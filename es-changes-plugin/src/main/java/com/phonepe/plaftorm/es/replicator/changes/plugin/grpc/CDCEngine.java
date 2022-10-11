package com.phonepe.plaftorm.es.replicator.changes.plugin.grpc;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.protobuf.ByteString;
import com.phonepe.platform.es.replicator.grpc.ChangesEngineGrpc;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.close.CloseIndexClusterStateUpdateRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingClusterStateUpdateRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexClusterStateUpdateRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.ack.ClusterStateUpdateResponse;
import org.elasticsearch.cluster.ack.OpenIndexClusterStateUpdateResponse;
import org.elasticsearch.cluster.metadata.*;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.ByteBufferStreamInput;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.engine.Engine.*;
import org.elasticsearch.index.shard.IndexShard;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.indices.IndicesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.phonepe.platform.es.replicator.grpc.Engine.*;

@Slf4j
public class CDCEngine extends ChangesEngineGrpc.ChangesEngineImplBase {
    private final IndicesService indicesService;
    private final NodeClient nodeClient;

    private final ClusterService clusterService;

    private final IndexScopedSettings indexScopedSettings;

    private final MetaDataMappingService metaDataMappingService;

    private final IndexNameExpressionResolver indexNameExpressionResolver;

    private final MetaDataIndexStateService indexStateService;

    @Inject
    public CDCEngine(IndicesService indicesService, NodeClient nodeClient, ClusterService clusterService,
                     IndexScopedSettings indexScopedSettings, MetaDataMappingService metaDataMappingService,
                     IndexNameExpressionResolver indexNameExpressionResolver, MetaDataIndexStateService metaDataIndexStateService) {
        this.indicesService = indicesService;
        this.nodeClient = nodeClient;
        this.clusterService = clusterService;
        this.indexScopedSettings = indexScopedSettings;
        this.metaDataMappingService = metaDataMappingService;
        this.indexNameExpressionResolver = indexNameExpressionResolver;
        this.indexStateService = metaDataIndexStateService;
    }


    @Override
    public void getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest request, StreamObserver<GetIndexAndShardsMetadataResponse> responseObserver) {
        ImmutableOpenMap<String, IndexMetaData> indices = nodeClient.admin()
                .cluster()
                .prepareState()
                .get()
                .getState()
                .getMetaData()
                .getIndices();

        List<ShardRouting> shardRoutings = new ArrayList<>(nodeClient.admin()
                .cluster()
                .prepareState()
                .get()
                .getState()
                .routingTable()
                .allShards());

        Map<String, List<ESShardRouting>> routingsMap = shardRoutings.stream()
                .map(CDCEngine::translateShardRouting)
                .collect(Collectors.groupingBy(ESShardRouting::getIndexName));

        List<ESIndexMetadata> indexMetadatas = new ArrayList<>();
        indices.valuesIt().forEachRemaining(indexMetaData -> {
            indexMetadatas.add(ESIndexMetadata.newBuilder()
                            .setIndexName(indexMetaData.getIndex().getName())
                            .setIndexUUID(indexMetaData.getIndexUUID())
                            .setNoOfShards(indexMetaData.getNumberOfShards())
                            .setNoOfRepliacs(indexMetaData.getNumberOfReplicas())
                            .setState(indexMetaData.getState().name())
                            .setMappingVersion(indexMetaData.getMappingVersion())
                            .addAllShardRoutings(routingsMap.getOrDefault(indexMetaData.getIndex().getName(), List.of()))
                    .build());
        });

        GetIndexAndShardsMetadataResponse response = GetIndexAndShardsMetadataResponse.newBuilder()
                .setCurrentNodeId(nodeClient.getLocalNodeId())
                .addAllIndexMetadatas(indexMetadatas)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private static ESShardRouting translateShardRouting(ShardRouting routing) {
        return ESShardRouting.newBuilder()
                .setShardId(routing.id())
                .setState(routing.state().name())
                .setNodeId(routing.currentNodeId() == null ? "NA" : routing.currentNodeId())
                .setPrimary(routing.primary())
                .setIndexName(routing.getIndexName())
                .setActive(routing.active())
                .setAssignedToNode(routing.assignedToNode())
                .build();
    }

    @Override
    public void getShardChanges(GetChangesRequest request, StreamObserver<GetChangesResponse> responseObserver) {
        log.error("Received shard changes request");
        ShardId shardId = new ShardId(request.getIndexName(), request.getIndexUUID(), request.getShardId());
        IndexShard indexShard = indicesService.indexServiceSafe(shardId.getIndex()).getShard(shardId.getId());
        List<SerializedTranslog> operations = getHistoryOfOperationsFromLucene(indexShard, request.getFromSeqNo(), request.getToSeqNo())
                .stream().map(op -> {
                    BytesStreamOutput streamOutput = new BytesStreamOutput();
                    try {
                        Translog.Operation.writeOperation(streamOutput, op);
                        return SerializedTranslog.newBuilder()
                                .setSeqNo(op.seqNo())
                                .setOpType(op.opType().name())
                                .setTranslogBytes(ByteString.copyFrom(streamOutput.bytes().toBytesRef().bytes))
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        log.info("Found {} operations", operations);
        GetChangesResponse response =  GetChangesResponse.newBuilder()
                .addAllChanges(operations)
                .setFromSeqNo(request.getFromSeqNo())
                .setLastSyncedGlobalCheckpoint(indexShard.getLastSyncedGlobalCheckpoint())
                .setMaxSeqNoOfUpdatesOrDeletes(indexShard.getMaxSeqNoOfUpdatesOrDeletes())
                .setFromSeqNo(request.getFromSeqNo())
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void applyTranslog(ApplyTranslogRequest request, StreamObserver<ApplyTranslogResponse> responseObserver) {
        if (!clusterService.state().routingTable().hasIndex(request.getShardId().getIndexName())) {
            responseObserver.onNext(ApplyTranslogResponse.newBuilder()
                    .setSuccess(false)
                    .setErrorCode(ApplyTranslogResponse.ErrorCode.INDEX_DOES_NOT_EXISTS)
                    .build());
            responseObserver.onCompleted();
            return;
        }

        IndexMetaData targetIndex = clusterService.state().getMetaData()
                .index(request.getShardId().getIndexName());

        // index uuid should be of the index in the replica cluster
        ShardId shardId = new ShardId(request.getShardId().getIndexName(), targetIndex.getIndexUUID(), request.getShardId().getShardId());
        IndexShard indexShard = indicesService.indexServiceSafe(shardId.getIndex()).getShard(shardId.getId());

        StreamInput streamInput = new ByteBufferStreamInput(request.getTranslog()
                .getTranslogBytes()
                .asReadOnlyByteBuffer());

        log.info("Before applying translog op, local checkpoint: {}, last synced global checkpoint: {}",
                indexShard.getLocalCheckpoint(), indexShard.getLastSyncedGlobalCheckpoint());

        try {
            Translog.Operation operation = translate(Translog.Operation.readOperation(streamInput), indexShard.getOperationPrimaryTerm());
            log.info("Applying operation of type: {}, seq: {}, primaryTerm: {}", operation.opType(), operation.seqNo(), operation.primaryTerm());

            Result result = indexShard.applyTranslogOperation(operation, Engine.Operation.Origin.REPLICA);
            log.info("Result: Type: {}, ", result.getResultType(), result.getFailure());

            ApplyTranslogResponse translogResponse = ApplyTranslogResponse.newBuilder()
                    .setError(result.getResultType().name())
                    .setSuccess(!(result.getResultType().equals(Engine.Result.Type.FAILURE) || result.getResultType().equals(Engine.Result.Type.MAPPING_UPDATE_REQUIRED)))
                    .setSequence(operation.seqNo())
                    .build();

            responseObserver.onNext(translogResponse);
            responseObserver.onCompleted();

        } catch (IOException e) {
            log.error("Error ", e);
            responseObserver.onError(e);
            responseObserver.onCompleted();
        }
    }

    private Translog.Operation translate(Translog.Operation operation, long primaryTerm) {
        switch (operation.opType()) {
            case INDEX:
                Translog.Index sourceOp = ((Translog.Index) operation);
                return new Translog.Index(
                        sourceOp.type(),
                        sourceOp.id(),
                        sourceOp.seqNo(),
                        primaryTerm,
                        sourceOp.version(),
                        BytesReference.toBytes(sourceOp.source()),
                        sourceOp.routing(),
                        sourceOp.getAutoGeneratedIdTimestamp()
                );
            case DELETE:
                Translog.Delete deleteOp = (Translog.Delete) operation;
                return new Translog.Delete(
                        deleteOp.type(),
                        deleteOp.id(),
                        deleteOp.uid(),
                        deleteOp.seqNo(),
                        primaryTerm,
                        deleteOp.version()
                );

            case NO_OP:
                Translog.NoOp noOp = (Translog.NoOp) operation;
                return new Translog.NoOp(noOp.seqNo(), primaryTerm, noOp.reason());
            default:
                break;
        }

        return null;
    }

    @SneakyThrows
    public List<Translog.Operation> getHistoryOfOperations(IndexShard indexShard, long startSeqNo, long toSeq) {
        log.info("Fetching translog snapshot for shard: {} - from {}", indexShard.shardId(), startSeqNo);
        Translog.Snapshot snapshot = indexShard.getHistoryOperations("cdc-plugin", startSeqNo);

        List<Translog.Operation> operations = new ArrayList<>();

        for(int i=0; i<snapshot.totalOperations(); i++) {
            Translog.Operation op = snapshot.next();
            if (op.seqNo() >= startSeqNo && op.seqNo() <= toSeq)
                operations.add(op);
        }

        snapshot.close();

        return operations;
    }
    @SneakyThrows
    public List<Translog.Operation> getHistoryOfOperationsFromLucene(IndexShard indexShard, long startSeqNo, long toSeq) {
        log.info("Fetching translog snapshot for shard: {} - from {}", indexShard.shardId(), startSeqNo);
        Translog.Snapshot snapshot = indexShard.newChangesSnapshot("cdc-plugin", startSeqNo, toSeq, false);

        List<Translog.Operation> operations = new ArrayList<>();

        for(int i=0; i<snapshot.totalOperations(); i++) {
            Translog.Operation op = snapshot.next();
            if (op.seqNo() >= startSeqNo)
                operations.add(op);
        }

        snapshot.close();

        return operations;
    }
    @Override
    @SneakyThrows
    public void getIndexMetadata(GetIndexMetadataRequest request, StreamObserver<GetIndexMetadataResponse> responseObserver) {
        log.info("Receivd index metadata request {}", request.toString());
        IndexMetaData indexMetaData = clusterService.state()
                .getMetaData()
                .index(request.getIndexName());

        BytesStreamOutput streamOutput = new BytesStreamOutput();
        Settings.writeSettingsToStream(indexMetaData.getSettings(), streamOutput);

        log.info("Aliases {}", indexMetaData.getAliases().toString());


        List<ESAliasMetadata> aliasMetaDatas = new ArrayList<>();
        indexMetaData.getAliases().values().forEach((Consumer<ObjectCursor<AliasMetaData>>) aliasMetaDataObjectCursor -> {
            AliasMetaData aliasMetaData = aliasMetaDataObjectCursor.value;
            // TODO(Praveen): Copy index filters too from source alias
            ESAliasMetadata esAliasMetadata = ESAliasMetadata.newBuilder()
                    .setAlias(aliasMetaData.getAlias())
                    .setIndexRouting(aliasMetaData.getIndexRouting())
                    .setWriteIndex(aliasMetaData.writeIndex())
                    .setSearchRouting(aliasMetaData.searchRouting())
                    .build();

            aliasMetaDatas.add(esAliasMetadata);
        });


        ESIndexMetadata indexMetadata = ESIndexMetadata.newBuilder()
                .setState(indexMetaData.getState().name())
                .setIndexUUID(indexMetaData.getIndexUUID())
                .setIndexName(indexMetaData.getIndex().getName())
                .setMappingVersion(indexMetaData.getMappingVersion())
                .setNoOfRepliacs(indexMetaData.getNumberOfReplicas())
                .setNoOfShards(indexMetaData.getNumberOfShards())
                .setMapping(ByteString.copyFrom(indexMetaData.mapping().source().compressed()))
                .setMappingType(indexMetaData.mapping().type())
                .setSettings(ByteString.copyFrom(streamOutput.bytes().toBytesRef().bytes))
                .addAllAliases(aliasMetaDatas)
                .build();

        GetIndexMetadataResponse response = GetIndexMetadataResponse.newBuilder()
                .setIndexMetadata(indexMetadata)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @SneakyThrows
    @Override
    public void createIndex(ESCreateIndexRequest request, StreamObserver<ESCreateIndexResponse> responseObserver) {
        CompressedXContent mappingContent = new CompressedXContent(request.getIndexMetadata().getMapping().toByteArray());
        MappingMetaData metaData = new MappingMetaData(mappingContent);

        log.info("Creating index with mapping type: {}, and source: {}", request.getIndexMetadata().getMappingType(),
                metaData.getSourceAsMap());


        Settings filetered = prepareSettings(request);


        CreateIndexRequest createIndexRequest = new CreateIndexRequest(request.getIndexMetadata().getIndexName())
                .mapping(request.getIndexMetadata().getMappingType(), metaData.getSourceAsMap())
                .settings(filetered);


        setAliases(request, createIndexRequest);


        nodeClient.admin().indices().create(createIndexRequest, new ActionListener<>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                log.info("Index creation ack {}", createIndexResponse.isAcknowledged());
                responseObserver.onNext(ESCreateIndexResponse.newBuilder()
                        .setSuccess(true)
                        .build());
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Error ", e);
                responseObserver.onError(e);
                responseObserver.onCompleted();
            }
        });
    }

    private Settings prepareSettings(ESCreateIndexRequest request) throws IOException {
        ByteBufferStreamInput streamInput = new ByteBufferStreamInput(request.getIndexMetadata().getSettings().asReadOnlyByteBuffer());
        Settings settings = Settings.readSettingsFromStream(streamInput);

        Settings.Builder settingsBuilder = Settings.builder();
        settings.keySet().forEach(settingKey -> {
            if (indexScopedSettings.isPrivateSetting(settingKey)) {
                log.info("Skipping setting as it is private {}", settingKey);
                return;
            }

            Setting<?> setting = indexScopedSettings.get(settingKey);
            if (!setting.isPrivateIndex()) {
                log.info("Setting applied {}", settingKey);
                settingsBuilder.copy(settingKey, settings);
            }
        });
        Settings filetered = settingsBuilder.build();

        filetered.keySet().forEach(setting -> {
            log.info("SEtting {} -> {}", setting, settings.get(setting));
        });
        return filetered;
    }

    private void setAliases(ESCreateIndexRequest request, CreateIndexRequest createIndexRequest) {
        request.getIndexMetadata().getAliasesList().forEach(aliasMetadata -> {
            createIndexRequest.alias(new Alias(aliasMetadata.getAlias())
                    .writeIndex(aliasMetadata.getWriteIndex())
                    .searchRouting(aliasMetadata.getSearchRouting())
                    .indexRouting(aliasMetadata.getIndexRouting())
            );
        });
    }

    @Override
    public void getMapping(GetMappingRequest request, StreamObserver<GetMappingResponse> responseObserver) {
        log.info("Received get mapping request {}", request.getIndexName());
        IndicesOptions options = IndicesOptions.strictSingleIndexNoExpandForbidClosed();

        GetMappingsRequest getMappingsRequest = new GetMappingsRequest()
                .indices(request.getIndexName())
                .indicesOptions(options);

        nodeClient.admin().indices().getMappings(getMappingsRequest, new ActionListener<>() {
            @Override
            public void onResponse(GetMappingsResponse getMappingsResponse) {
                log.info("Mapping s: {}", getMappingsResponse.mappings().toString());
                responseObserver.onNext(GetMappingResponse.newBuilder()
                                .setIndexName(request.getIndexName())
                                .setMappingSource(getMappingsResponse.getMappings()
                                        .get(request.getIndexName())
                                        .get("_doc")
                                        .source().string())
                        .build());
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Error fetching mapping ", e);
                responseObserver.onError(e);
            }
        });
    }

    @Override
    public void syncMapping(ApplyMappingRequest request, StreamObserver<ApplyMappingResponse> responseObserver) {
        Index[] indices = resolveIndices(request.getIndexName());

        PutMappingClusterStateUpdateRequest updateRequest = new PutMappingClusterStateUpdateRequest()
                .source(request.getMappingSource())
                .type("_doc")
                .ackTimeout(new TimeValue(5, TimeUnit.SECONDS))
                .masterNodeTimeout(new TimeValue(5, TimeUnit.SECONDS))
                .indices(indices);

        metaDataMappingService.putMapping(updateRequest, new ActionListener<>() {
            @Override
            public void onResponse(ClusterStateUpdateResponse clusterStateUpdateResponse) {
                responseObserver.onNext(ApplyMappingResponse.newBuilder()
                        .setSuccess(clusterStateUpdateResponse.isAcknowledged())
                        .build());
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Error applying mapping ", e);
                responseObserver.onError(e);
            }
        });
    }

    private Index[] resolveIndices(String... indexNames) {
        IndicesOptions options = IndicesOptions.strictSingleIndexNoExpandForbidClosed();
        IndicesRequest indicesRequest = new IndicesRequest() {
            @Override
            public String[] indices() {
                return indexNames;
            }

            @Override
            public IndicesOptions indicesOptions() {
                return options;
            }
        };

        return indexNameExpressionResolver.concreteIndices(clusterService.state(), indicesRequest);
    }

    @Override
    public void openIndices(OpenIndicesRequest request, StreamObserver<OpenIndicesResponse> responseObserver) {
        Index[] concreteIndices = resolveIndices(request.getIndexNameList().toArray(new String[]{}));
        OpenIndexClusterStateUpdateRequest updateRequest = new OpenIndexClusterStateUpdateRequest()
                .ackTimeout(TimeValue.timeValueSeconds(5))
                .masterNodeTimeout(TimeValue.timeValueSeconds(5))
                .indices(concreteIndices)
                .waitForActiveShards(ActiveShardCount.ALL);

        indexStateService.openIndex(updateRequest, new ActionListener<>() {
            @Override
            public void onResponse(OpenIndexClusterStateUpdateResponse openIndexClusterStateUpdateResponse) {
                responseObserver.onNext(OpenIndicesResponse.newBuilder()
                                .setSuccess(openIndexClusterStateUpdateResponse.isAcknowledged())
                        .build());
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Error opening the indices ", e);
                responseObserver.onError(e);
            }
        });
    }

    @Override
    public void closeIndices(CloseIndicesRequest request, StreamObserver<CloseIndicesResponse> responseObserver) {
        Index[] indices = resolveIndices(request.getIndexNameList().toArray(new String[]{}));

        CloseIndexClusterStateUpdateRequest closeRequest = new  CloseIndexClusterStateUpdateRequest(1)
                .masterNodeTimeout(TimeValue.timeValueSeconds(5))
                .indices(indices)
                .ackTimeout(TimeValue.timeValueSeconds(5))
                .waitForActiveShards(ActiveShardCount.ALL);


        indexStateService.closeIndices(closeRequest, new ActionListener<>() {
            @Override
            public void onResponse(CloseIndexResponse closeIndexResponse) {
                responseObserver.onNext(CloseIndicesResponse.newBuilder()
                                .setSuccess(closeIndexResponse.isAcknowledged())
                        .build());
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Error closing indices ", e);
                responseObserver.onError(e);
            }
        });
    }
}
