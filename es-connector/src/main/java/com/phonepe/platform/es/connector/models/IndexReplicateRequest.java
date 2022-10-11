package com.phonepe.platform.es.connector.models;

import com.phonepe.platform.es.replicator.grpc.Engine;
import com.phonepe.platform.es.replicator.grpc.Engine.ESIndexMetadata;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class IndexReplicateRequest {
    String currentNodeId;

    ESIndexMetadata indexMetadata;
}
