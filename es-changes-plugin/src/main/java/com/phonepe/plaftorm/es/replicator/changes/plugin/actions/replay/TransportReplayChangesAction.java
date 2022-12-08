package com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay;

import com.phonepe.plaftorm.es.replicator.changes.plugin.exceptions.MappingUpdateRequiredException;
import com.phonepe.platform.es.replicator.models.ApplyTranslogResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.replication.TransportWriteAction;
import org.elasticsearch.cluster.action.shard.ShardStateAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.mapper.MapperParsingException;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.shard.IndexShard;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class TransportReplayChangesAction extends TransportWriteAction<ReplayChangesReq, ReplayChangesReq, ReplayChangesResponse> {

    @Inject
    public TransportReplayChangesAction(final Settings settings,
                                        final TransportService transportService,
                                        final ClusterService clusterService,
                                        final IndicesService indicesService,
                                        final ThreadPool threadPool,
                                        final ShardStateAction shardStateAction,
                                        final ActionFilters actionFilters,
                                        final IndexNameExpressionResolver indexNameExpressionResolver) {
        super(settings,
                ReplayChangesAction.NAME,
                transportService,
                clusterService,
                indicesService,
                threadPool,
                shardStateAction,
                actionFilters,
                indexNameExpressionResolver,
                ReplayChangesReq::new,
                ReplayChangesReq::new,
                ThreadPool.Names.WRITE,
                false);
    }

    @Override
    protected ReplayChangesResponse newResponseInstance() {
        return new ReplayChangesResponse();
    }

    @Override
    protected void shardOperationOnPrimary(final ReplayChangesReq request, final IndexShard primaryShard, final ActionListener<PrimaryResult<ReplayChangesReq, ReplayChangesResponse>> listener) {
        // TODO Check index blocks

        Translog.Location location = null;
        long lastSuccessfulSequence = -1;

        for(Translog.Operation incomingOp: request.getChanges()) {
            Translog.Operation operation = translate(incomingOp, primaryShard.getOperationPrimaryTerm());
            try {
                if (primaryShard.getMaxSeqNoOfUpdatesOrDeletes() < request.getMaxSeqNoOfUpdatesOrDeletes()) {
                    primaryShard.advanceMaxSeqNoOfUpdatesOrDeletes(request.getMaxSeqNoOfUpdatesOrDeletes());
                }

                val result = primaryShard.applyTranslogOperation(operation, Engine.Operation.Origin.PRIMARY);

                if (requiresMappingUpdate(result)) {
                    val response = new ReplayChangesResponse();
                    response.setSuccess(false);
                    response.setLastSuccessfulSeqNo(lastSuccessfulSequence);
                    response.setErrorCode(ApplyTranslogResponse.ErrorCode.MAPPING_UPDATE_REQUIRED);

                    log.info("Mapping updated is required");
                    listener.onResponse(new WritePrimaryResult<>(request, response, location,
                            new MappingUpdateRequiredException(primaryShard.shardId()),
                            primaryShard, logger));
                    return;
                }

                lastSuccessfulSequence = operation.seqNo();
                location = syncOperationResultOrThrow(result, location);

            } catch (Exception e) {
                val response = new ReplayChangesResponse();
                response.setSuccess(false);
                response.setErrorCode(ApplyTranslogResponse.ErrorCode.UNKNOWN);
                log.error("Error applying translog ", e);
                listener.onResponse(new WritePrimaryResult<>(request, response, location, e, primaryShard, logger));
                return;
            }
        }

        val response = new ReplayChangesResponse();
        response.setSuccess(true);
        response.setLastSuccessfulSeqNo(lastSuccessfulSequence);

        listener.onResponse(new WritePrimaryResult<>(request, response, location, null, primaryShard, logger));
    }

    @Override
    protected WriteReplicaResult<ReplayChangesReq> shardOperationOnReplica(final ReplayChangesReq request, final IndexShard replicaShard) throws Exception {

        log.info("Applying operation on replica, ");
        final AtomicReference<Translog.Location> location = new AtomicReference<>();

        for(Translog.Operation incomingOp: request.getChanges()) {
            Translog.Operation operation = translate(incomingOp, replicaShard.getOperationPrimaryTerm());

            if (replicaShard.getMaxSeqNoOfUpdatesOrDeletes() < request.getMaxSeqNoOfUpdatesOrDeletes()) {
                replicaShard.advanceMaxSeqNoOfUpdatesOrDeletes(request.getMaxSeqNoOfUpdatesOrDeletes());
            }

            if (incomingOp.opType() == Translog.Operation.Type.INDEX) {
                Translog.Index indexOp = (Translog.Index) incomingOp;
                log.info("Applying id: {}, seqNo: {}, routing: {}, version: {}", indexOp.id(), indexOp.seqNo(), indexOp.routing(), indexOp.version());

            }

            val result = replicaShard.applyTranslogOperation(operation, Engine.Operation.Origin.REPLICA);
            if (requiresMappingUpdate(result)) {
                log.info("Apply Result : reqdMappUpdate {}, resultType: {}, source: {}", result.getRequiredMappingUpdate(), result.getResultType(),
                        operation.getSource().source.utf8ToString(), result.getFailure());
                return new WriteReplicaResult<>(request, location.get(),  null,
                        replicaShard, logger);
            }

            location.set(syncOperationResultOrThrow(result, location.get()));
        }

        return new WriteReplicaResult<>(request, location.get(), null, replicaShard, logger);
    }

    boolean requiresMappingUpdate(Engine.Result result) {
        return result.getResultType() == Engine.Result.Type.MAPPING_UPDATE_REQUIRED || result.getFailure() instanceof MapperParsingException;
    }

    private Translog.Operation translate(Translog.Operation operation, long operationPrimaryTerm) {
        switch (operation.opType()) {
            case CREATE:
            case INDEX:
                Translog.Index sourceOp = (Translog.Index) operation;
                return new Translog.Index(
                        sourceOp.type(),
                        sourceOp.id(),
                        sourceOp.seqNo(),
                        operationPrimaryTerm,
                        sourceOp.version(),
                        sourceOp.source().toBytesRef().bytes,
                        sourceOp.routing(),
                        IndexRequest.UNSET_AUTO_GENERATED_TIMESTAMP);
            case DELETE:
                Translog.Delete deleteOp = (Translog.Delete) operation;
                return new Translog.Delete(Engine.Operation.TYPE.DELETE.name(),
                        deleteOp.id(),
                        deleteOp.uid(),
                        deleteOp.seqNo(),
                        operationPrimaryTerm,
                        deleteOp.version());
            case NO_OP:
                Translog.NoOp noOp = (Translog.NoOp) operation;
                return new Translog.NoOp(noOp.seqNo(), operationPrimaryTerm, noOp.reason());
        }

        throw new RuntimeException("Unrecognized operationType");
    }




}
