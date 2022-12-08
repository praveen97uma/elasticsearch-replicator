package com.phonepe.platform.es.connector.factories;

import com.phonepe.platform.es.connector.models.ShardReplicateRequest;
import com.phonepe.platform.es.connector.tasks.MetadataPollingTask;
import com.phonepe.platform.es.connector.tasks.ShardReplicationTask;

public interface MetadatPollerTaskFactory {
    MetadataPollingTask create(String indexName);
}
