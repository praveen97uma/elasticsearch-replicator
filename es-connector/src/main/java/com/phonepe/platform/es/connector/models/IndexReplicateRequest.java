package com.phonepe.platform.es.connector.models;

import com.phonepe.platform.es.replicator.models.EsIndexMetadata;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class IndexReplicateRequest {
    String currentNodeId;

    EsIndexMetadata indexMetadata;
}
