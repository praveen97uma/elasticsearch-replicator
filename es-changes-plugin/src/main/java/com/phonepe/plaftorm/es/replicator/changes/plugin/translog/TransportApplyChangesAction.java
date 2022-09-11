package com.phonepe.plaftorm.es.replicator.changes.plugin.translog;

import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesRequest;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesResponse;
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
        super(GetChangesAction.NAME, threadPool, clusterService, transportService, actionFilters, indexNameExpressionResolver, ApplyChangesRequest::new, "xx");
        this.indicesService = indicesService;
        TransportActionProxy.registerProxyAction(transportService, GetChangesAction.NAME, GetChangesResponse::new);
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
                            .forEach(operation -> {
                                try {
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
}
