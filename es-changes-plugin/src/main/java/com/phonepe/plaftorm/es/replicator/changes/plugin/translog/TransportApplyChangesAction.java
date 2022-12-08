package com.phonepe.plaftorm.es.replicator.changes.plugin.translog;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.single.shard.TransportSingleShardAction;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.ShardsIterator;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.shard.IndexShard;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportActionProxy;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TransportApplyChangesAction extends TransportSingleShardAction<ApplyChangesRequest, ApplyChangesResponse> {
    private final IndicesService indicesService;

    @Inject
    public TransportApplyChangesAction(ThreadPool threadPool, ClusterService clusterService, TransportService transportService, ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver, IndicesService indicesService) {
        super(ApplyChangesAction.NAME, threadPool, clusterService, transportService, actionFilters, indexNameExpressionResolver, ApplyChangesRequest::new, "xx");
        this.indicesService = indicesService;
        TransportActionProxy.registerProxyAction(transportService, ApplyChangesAction.NAME, ApplyChangesResponse::new);
    }

    @Override
    protected ApplyChangesResponse shardOperation(ApplyChangesRequest request, ShardId shardId) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void asyncShardOperation(ApplyChangesRequest request, ShardId shardId, ActionListener<ApplyChangesResponse> listener) throws IOException {
        log.info("Shard operation running");
        try {
            IndexShard indexShard = indicesService.indexServiceSafe(shardId.getIndex()).getShard(shardId.getId());
            request.getChanges()
                    .forEach(incoming -> {
                        try {
                            Translog.Operation operation = translate(incoming, indexShard.getOperationPrimaryTerm());
                            log.info("Applying translog with seqNo: {}, opType: {}, primaryTerm: {}",
                                    operation.seqNo(), operation.opType(), operation.primaryTerm());
                            indexShard.applyTranslogOperation(operation, Engine.Operation.Origin.PRIMARY);
                        } catch (Exception e) {
                            log.error("Error applying translog ", e);
                        }
                    });
            listener.onResponse(new ApplyChangesResponse(true));


        } catch (Exception e) {
            log.error("Error ", e);
            listener.onFailure(e);
        }
    }

    @Override
    protected Writeable.Reader<ApplyChangesResponse> getResponseReader() {
        return ApplyChangesResponse::new;
    }

    @Override
    protected boolean resolveIndex(ApplyChangesRequest request) {
        return true;
    }

    @Override
    protected ShardsIterator shards(ClusterState state, TransportSingleShardAction<ApplyChangesRequest, ApplyChangesResponse>.InternalRequest request) {
        log.info("Computing shards");
        state.routingTable().allShards().forEach(shardRouting -> {
            log.info("Shard index name: {}, id: {}, indexUUID: {}, indexName: {}", shardRouting.shardId().getIndexName(), shardRouting.shardId().getId(), shardRouting.index().getUUID(), shardRouting.index().getName());
        });
        IndexShardRoutingTable table =  state.routingTable().shardRoutingTable(request.request().getShardId());
        log.info("Shard routing table selected {}", table.getActiveShards());
        return table.activeInitializingShardsRandomIt();
    }

    @SneakyThrows
    public List<Translog.Operation> getHistoryOfOperations(IndexShard indexShard, long startSeqNo, long toSeqNo) {
        log.trace("Fetching translog snapshot for $indexShard - from $startSeqNo to $toSeqNo");
        // Ref issue: https://github.com/opensearch-project/OpenSearch/issues/2482
        Translog.Snapshot snapshot = indexShard.getHistoryOperations("cdc-plugin", startSeqNo);

        // Total ops to be fetched (both toSeqNo and startSeqNo are inclusive)
//        val opsSize = toSeqNo - startSeqNo + 1
//        val ops = ArrayList<Translog.Operation>(opsSize.toInt());

        List<Translog.Operation> operations = new ArrayList<>();

        for(int i=0; i<snapshot.totalOperations(); i++) {
            operations.add(snapshot.next());
        }

        return operations;
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
}
