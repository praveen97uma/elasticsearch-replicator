package com.phonepe.platform.es.client;


import com.phonepe.platform.es.replicator.models.*;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.GetMappingsResponse;

import java.io.IOException;

public interface ESClient {
    GetIndexAndShardsMetadataResponse getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest request) throws IOException;

    EsGetChangesResponse getShardChanges(GetChangesRequest request) throws IOException;

    ApplyTranslogResponse replayShardChanges(ApplyTranslogRequest request) throws IOException;

    GetMappingsResponse getMappings(String indexName) throws IOException;

    AcknowledgedResponse createMappings(String indexName, String mappingSource) throws IOException;

    CreateIndexResponse createIndex(CreateIndexRequest createIndexRequest) throws IOException;
}
