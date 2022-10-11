package com.phonepe.platform.es.connector.store;

import java.util.Optional;

public interface TranslogCheckpointStore {
    void saveCheckpoint(ShardCheckpoint checkpoint);
    Optional<ShardCheckpoint> getCheckpoint(String indexName, int shardId);
}
