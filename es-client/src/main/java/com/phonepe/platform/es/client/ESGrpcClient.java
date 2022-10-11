package com.phonepe.platform.es.client;

import com.phonepe.platform.es.replicator.grpc.ChangesEngineGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static com.phonepe.platform.es.replicator.grpc.Engine.*;

public class ESGrpcClient implements ESClient {
    private static ChangesEngineGrpc.ChangesEngineBlockingStub stub;

    public ESGrpcClient(ESClientConfiguration clientConfiguration) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(clientConfiguration.getHost(), clientConfiguration.getPort())
                .usePlaintext()
                .build();

        stub = ChangesEngineGrpc.newBlockingStub(channel);
    }

    @Override
    public GetIndexAndShardsMetadataResponse getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest request) {
        return stub.getIndexAndShardsMetadata(request);
    }

    @Override
    public GetChangesResponse getShardChanges(GetChangesRequest request) {
        return stub.getShardChanges(request);
    }

    @Override
    public ApplyTranslogResponse applyTranslog(ApplyTranslogRequest request) {
        return stub.applyTranslog(request);
    }

    @Override
    public GetIndexMetadataResponse getIndexMetadata(GetIndexMetadataRequest request) {
        return stub.getIndexMetadata(request);
    }

    @Override
    public ESCreateIndexResponse createIndex(ESCreateIndexRequest request) {
        return stub.createIndex(request);
    }

    @Override
    public ApplyMappingResponse syncMapping(ApplyMappingRequest request) {
        return stub.syncMapping(request);
    }

    @Override
    public GetMappingResponse getMapping(GetMappingRequest request) {
        return stub.getMapping(request);
    }

    @Override
    public CloseIndicesResponse closeIndices(CloseIndicesRequest request) {
        return stub.closeIndices(request);
    }

    @Override
    public OpenIndicesResponse openIndices(OpenIndicesRequest request) {
        return stub.openIndices(request);
    }
}
