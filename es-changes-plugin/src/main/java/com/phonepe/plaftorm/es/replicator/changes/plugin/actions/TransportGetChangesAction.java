package com.phonepe.plaftorm.es.replicator.changes.plugin.actions;

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
import org.elasticsearch.tasks.Task;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportActionProxy;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

//    @Inject
//    public TransportGetChangesAction(TransportService transportService, ActionFilters actionFilters, TaskManager taskManager, IndicesService indicesService) {
//        super(GetChangesAction.NAME, actionFilters, taskManager);
//        this.indicesService = indicesService;

//    }

    @Override
    protected GetChangesResponse shardOperation(GetChangesRequest request, ShardId shardId) throws IOException {
//        log.info("Shard operation running");
//        IndexShard indexShard = indicesService.indexServiceSafe(shardId.getIndex()).getShard(shardId.getId());
//        List<Translog.Operation> operations = getHistoryOfOperations(indexShard, request.getFromSeqNo(), 10000);
//        operations.forEach(op -> {
//            log.info("{}, {}, {}, {}", op.seqNo(), op.getSource(), op.opType(), op.getSource());
//        });
//
//        log.info("Found {} operations", operations);
//        return new GetChangesResponse(operations, request.getFromSeqNo(), indexShard.getMaxSeqNoOfUpdatesOrDeletes(), indexShard.getLastSyncedGlobalCheckpoint());
        throw new UnsupportedOperationException();
    }

    @Override
    protected void asyncShardOperation(GetChangesRequest request, ShardId shardId, ActionListener<GetChangesResponse> listener) throws IOException {
        log.info("FromSeq: {}, {}", request.getFromSeqNo(), request.getToSeqNo());

        log.info("Shard operation running");
        try {
            IndexShard indexShard = indicesService.indexServiceSafe(shardId.getIndex()).getShard(shardId.getId());
            List<Translog.Operation> operations = getHistoryOfOperations(indexShard, request.getFromSeqNo(), 10000);
            operations.forEach(op -> {
                log.info("{}, {}, {}, {}", op.seqNo(), op.getSource(), op.opType(), op.getSource());
            });

            log.info("Found {} operations", operations);
            listener.onResponse(new GetChangesResponse(operations, request.getFromSeqNo(), indexShard.getMaxSeqNoOfUpdatesOrDeletes(), indexShard.getLastSyncedGlobalCheckpoint()));


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
}
