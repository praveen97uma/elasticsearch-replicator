package com.phonepe.platform.es.client;


import com.phonepe.platform.es.replicator.models.GetChangesRequest;
import com.phonepe.platform.es.replicator.models.EsGetChangesResponse;
import com.phonepe.platform.es.replicator.models.GetIndexAndShardsMetadataRequest;
import com.phonepe.platform.es.replicator.models.GetIndexAndShardsMetadataResponse;

import java.io.IOException;

public interface ESClient {
    GetIndexAndShardsMetadataResponse getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest request) throws IOException;

    EsGetChangesResponse getShardChanges(GetChangesRequest request) throws IOException;
}
