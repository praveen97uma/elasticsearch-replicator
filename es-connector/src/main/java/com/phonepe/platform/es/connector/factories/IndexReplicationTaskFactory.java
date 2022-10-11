package com.phonepe.platform.es.connector.factories;

import com.phonepe.platform.es.connector.models.IndexReplicateRequest;
import com.phonepe.platform.es.connector.tasks.IndexReplicationTask;

public interface IndexReplicationTaskFactory {
    IndexReplicationTask create(IndexReplicateRequest request);
}
