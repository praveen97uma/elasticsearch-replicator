package com.phonepe.plaftorm.es.replicator.changes.plugin.actions.changes;

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
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.Writeable;
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
public class TransportGetChangesAction extends TransportSingleShardAction<GetChangesRequest, GetChangesResponse> {
    private final IndicesService indicesService;

    @Inject
    public TransportGetChangesAction(ThreadPool threadPool, ClusterService clusterService, TransportService transportService, ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver, IndicesService indicesService) {
        super(GetChangesAction.NAME, threadPool, clusterService, transportService, actionFilters, indexNameExpressionResolver, GetChangesRequest::new, "xx");
        this.indicesService = indicesService;
        TransportActionProxy.registerProxyAction(transportService, GetChangesAction.NAME, GetChangesResponse::new);
    }

    @Override
    protected GetChangesResponse shardOperation(GetChangesRequest request, ShardId shardId) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void asyncShardOperation(GetChangesRequest request, ShardId shardId, ActionListener<GetChangesResponse> listener) throws IOException {
        try {
            IndexShard indexShard = indicesService.indexServiceSafe(shardId.getIndex()).getShard(shardId.getId());


            if (indexShard.getLastSyncedGlobalCheckpoint() < request.getFromSeqNo()) {
                log.info("Last synced global checkpoint: {}, RequstedFromSeqNo: {}", indexShard.getLastSyncedGlobalCheckpoint(), request.getFromSeqNo());
                listener.onResponse(new GetChangesResponse(
                        List.of(),
                        request.getFromSeqNo(),
                        indexShard.getMaxSeqNoOfUpdatesOrDeletes(),
                        indexShard.getLastSyncedGlobalCheckpoint()));
                return;
            }

            long toSeq = Math.min(indexShard.getLocalCheckpoint(), request.getToSeqNo());

            List<Translog.Operation> operations = getHistoryOfOperations(indexShard, request.getFromSeqNo(), toSeq);


            log.info("Found {} operations", operations);
            listener.onResponse(new GetChangesResponse(
                    operations,
                    request.getFromSeqNo(),
                    indexShard.getMaxSeqNoOfUpdatesOrDeletes(),
                    indexShard.getLastSyncedGlobalCheckpoint()));


        } catch (Exception e) {
            log.error("Error ", e);
            listener.onFailure(e);
        }
    }

    @Override
    protected Writeable.Reader<GetChangesResponse> getResponseReader() {
        return GetChangesResponse::new;
    }

    @Override
    protected boolean resolveIndex(GetChangesRequest request) {
        return true;
    }

    @Override
    protected ShardsIterator shards(ClusterState state, TransportSingleShardAction<GetChangesRequest, GetChangesResponse>.InternalRequest request) {
        IndexShardRoutingTable table =  state.routingTable().shardRoutingTable(request.request().getShardId());
        log.info("Shard routing table selected {}", table.getActiveShards());
        return table.activeInitializingShardsRandomIt();
    }

    @SneakyThrows
    public List<Translog.Operation> getHistoryOfOperations(IndexShard indexShard, long startSeqNo, long toSeqNo) {
        log.debug("Fetching translog snapshot for indexShard: {} - from startSeqNo:{} to toSeqNo: {}, " +
                "localCheckpoint: {}. globalCP: {}, maxSeqNo: {}", indexShard.shardId(), startSeqNo, toSeqNo,
                indexShard.seqNoStats().getLocalCheckpoint(), indexShard.seqNoStats().getGlobalCheckpoint(),
                indexShard.seqNoStats().getMaxSeqNo());
        // Ref issue: https://github.com/opensearch-project/OpenSearch/issues/2482
//        Translog.Snapshot snapshot = indexShard.getHistoryOperations("cdc-plugin", startSeqNo);
        Translog.Snapshot snapshot = indexShard.newChangesSnapshot("cdc-plugin", startSeqNo, toSeqNo, false);
        log.debug("Total operations fetched: {} for indexShard: {}", snapshot.totalOperations(), indexShard.shardId());
        List<Translog.Operation> operations = new ArrayList<>();

        for(int i=0; i<snapshot.totalOperations(); i++) {
            Translog.Operation op = snapshot.next();
            if (op.seqNo() >= startSeqNo && op.seqNo() <= toSeqNo)
                operations.add(op);
        }

        log.debug("Total operations collected: {} for indexShard: {}", operations.size(), indexShard.shardId());
        snapshot.close();

        return operations;
    }
}
