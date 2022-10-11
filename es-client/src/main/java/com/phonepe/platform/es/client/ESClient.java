package com.phonepe.platform.es.client;

import com.phonepe.platform.es.replicator.grpc.Engine;

public interface ESClient {
    Engine.GetIndexAndShardsMetadataResponse getIndexAndShardsMetadata(Engine.GetIndexAndShardsMetadataRequest request);

    Engine.GetChangesResponse getShardChanges(Engine.GetChangesRequest request);

    Engine.ApplyTranslogResponse applyTranslog(Engine.ApplyTranslogRequest request);

    Engine.GetIndexMetadataResponse getIndexMetadata(Engine.GetIndexMetadataRequest request);

    Engine.ESCreateIndexResponse createIndex(Engine.ESCreateIndexRequest request);

    Engine.ApplyMappingResponse syncMapping(Engine.ApplyMappingRequest request);

    Engine.GetMappingResponse getMapping(Engine.GetMappingRequest request);

    Engine.CloseIndicesResponse closeIndices(Engine.CloseIndicesRequest request);

    Engine.OpenIndicesResponse openIndices(Engine.OpenIndicesRequest request);
}
