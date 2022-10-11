package com.phonepe.platform.es.connector.replication;

import com.phonepe.platform.es.replicator.grpc.Engine;

import static com.phonepe.platform.es.replicator.grpc.Engine.*;

public class ReplicationService {
    public void process(GetIndexAndShardsMetadataResponse metadata) {
        String currentNodeId = metadata.getCurrentNodeId();



    }
}
