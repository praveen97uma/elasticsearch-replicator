package com.phonepe.platform.es.connector.store;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ShardCheckpoint {
    String indexName;
    int shardId;
    long sequence;
    long timestamp;
}
