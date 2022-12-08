package com.phonepe.platform.es.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.es.replicator.models.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;


@Slf4j
public class ESRestClientImpl extends RestHighLevelClient implements ESClient {
//    private final ESClientConfiguration clientConfiguration;
    private final ObjectMapper mapper;

    private final RestClient restClient;

    private final RestHighLevelClient restHighLevelClient;

    public ESRestClientImpl(RestClientBuilder restClientBuilder) {
        super(restClientBuilder);
//        this.clientConfiguration = clientConfiguration;
        this.mapper = new ObjectMapper();
        this.restClient = restClientBuilder.build();
        this.restHighLevelClient = new RestHighLevelClient(restClientBuilder);

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
        req.addParameter("toSeqNo", String.valueOf(request.getToSeqNo()));

        log.debug("Getting changes for indexName: {}, ShardId: {}, FromSeqNo: {}, ToSeqNo: {}", request.getIndexName(),
                request.getShardId(), request.getFromSeqNo(), request.getToSeqNo());
        Response response = restClient.performRequest(req);

        return mapper.readValue(response.getEntity().getContent(), EsGetChangesResponse.class);
    }

    @Override
    public ApplyTranslogResponse replayShardChanges(final ApplyTranslogRequest request) throws IOException {

        return performRequestAndParseEntity(
                request,
                ReauestConverters::applyTranslog,
                RequestOptions.DEFAULT,
                ApplyTranslogResponse::fromXContent,
                Set.of());
    }


    static HttpEntity createEntity(ToXContent toXContent, XContentType xContentType) throws IOException {
        return createEntity(toXContent, xContentType, ToXContent.EMPTY_PARAMS);
    }

    static HttpEntity createEntity(ToXContent toXContent, XContentType xContentType, ToXContent.Params toXContentParams)
            throws IOException {
        BytesRef source = XContentHelper.toXContent(toXContent, xContentType, toXContentParams, false).toBytesRef();
        return new NByteArrayEntity(source.bytes, source.offset, source.length, createContentType(xContentType));
    }

    public static ContentType createContentType(final XContentType xContentType) {
        return ContentType.create(xContentType.mediaTypeWithoutParameters(), (Charset) null);
    }

    public GetMappingsResponse getMappings(String indexName) throws IOException {
        val options = IndicesOptions.strictSingleIndexNoExpandForbidClosed();
        val getMappingsRequest = new GetMappingsRequest()
                .indices(indexName)
                .indicesOptions(options);

        return restHighLevelClient.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
    }


    public AcknowledgedResponse createMappings(String indexName, String mappingSource) throws IOException {
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName)
                .indicesOptions(IndicesOptions.strictSingleIndexNoExpandForbidClosed())
                .source(mappingSource, XContentType.JSON);


        return restHighLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
    }

    @Override
    public CreateIndexResponse createIndex(final CreateIndexRequest createIndexRequest) throws IOException {
        return performRequestAndParseEntity(
                createIndexRequest,
                ReauestConverters::createIndex,
                RequestOptions.DEFAULT,
                CreateIndexResponse::fromXContent,
                Set.of());
    }
}
