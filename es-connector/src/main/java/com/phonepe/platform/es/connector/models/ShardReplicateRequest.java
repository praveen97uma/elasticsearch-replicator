package com.phonepe.platform.es.connector.models;

import com.phonepe.platform.es.replicator.models.EsIndexMetadata;
import com.phonepe.platform.es.replicator.models.EsShardRouting;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShardReplicateRequest {
    String currentNodeId;

    EsIndexMetadata indexMetadata;
    EsShardRouting shardRouting;

    public String getShardId() {
        return fromShardRouting(shardRouting);
    }

    public static String getShardId(String indexName, int shardId) {
        return String.format("%s:%d", indexName, shardId);
    }

    public static String fromShardRouting(EsShardRouting routing) {
        return getShardId(routing.getIndexName(), routing.getShardId());
    }
}
