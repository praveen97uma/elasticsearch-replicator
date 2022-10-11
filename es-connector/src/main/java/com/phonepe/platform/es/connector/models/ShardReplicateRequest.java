package com.phonepe.platform.es.connector.models;

import com.phonepe.platform.es.replicator.grpc.Engine;
import com.phonepe.platform.es.replicator.grpc.Engine.ESShardRouting;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShardReplicateRequest {
    String currentNodeId;

    Engine.ESIndexMetadata indexMetadata;
    ESShardRouting shardRouting;

    public String getShardId() {
        return fromShardRouting(shardRouting);
    }

    public static String getShardId(String indexName, int shardId) {
        return String.format("%s:%d", indexName, shardId);
    }

    public static String fromShardRouting(ESShardRouting routing) {
        return getShardId(routing.getIndexName(), routing.getShardId());
    }
}
