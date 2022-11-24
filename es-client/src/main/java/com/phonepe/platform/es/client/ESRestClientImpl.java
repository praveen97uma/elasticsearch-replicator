package com.phonepe.platform.es.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.es.replicator.models.GetChangesRequest;
import com.phonepe.platform.es.replicator.models.EsGetChangesResponse;
import com.phonepe.platform.es.replicator.models.GetIndexAndShardsMetadataRequest;
import com.phonepe.platform.es.replicator.models.GetIndexAndShardsMetadataResponse;
import org.apache.http.HttpHost;
import org.elasticsearch.client.*;

import java.io.IOException;

public class ESRestClientImpl implements ESClient {
    private final ESClientConfiguration clientConfiguration;
    private final ObjectMapper mapper;

    private final RestClient restClient;

    public ESRestClientImpl(final ESClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        this.mapper = new ObjectMapper();
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(clientConfiguration.getHost(), clientConfiguration.getPort(), "http"));

        this.restClient = restClientBuilder.build();
    }

    @Override
    public GetIndexAndShardsMetadataResponse getIndexAndShardsMetadata(final GetIndexAndShardsMetadataRequest request) throws IOException {

        Request req = new Request("GET", "_index/metadata");
        req.addParameter("indexName", "temp");

        Response response = restClient.performRequest(req);

        return mapper.readValue(response.getEntity().getContent(), GetIndexAndShardsMetadataResponse.class);
    }

    @Override
    public EsGetChangesResponse getShardChanges(final GetChangesRequest request) throws IOException {


        Request req = new Request("GET", "index/shard/changes");
        req.addParameter("indexName", request.getIndexName());
        req.addParameter("indexUUID", request.getIndexUUID());
        req.addParameter("shardId", String.valueOf(request.getShardId()));
        req.addParameter("fromSeqNo", String.valueOf(request.getFromSeqNo()));
//        req.addParameter("toSeqNo", String.valueOf(request.getToSeqNo()));

        Response response = restClient.performRequest(req);

        return mapper.readValue(response.getEntity().getContent(), EsGetChangesResponse.class);
    }


    public static void main(String... args) throws Exception {
        ESRestClientImpl client = new ESRestClientImpl(ESClientConfiguration.builder()
                .host("localhost")
                .port(9200)
                .build());
        client.getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest.builder().build());
    }
}
