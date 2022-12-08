package com.phonepe.platform.es.client;

import com.phonepe.platform.es.replicator.models.ApplyTranslogRequest;
import com.phonepe.platform.es.replicator.models.CreateIndexRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.nio.charset.Charset;


public class ReauestConverters {
    static final XContentType REQUEST_BODY_CONTENT_TYPE = XContentType.JSON;

    static Request applyTranslog(ApplyTranslogRequest applyTranslogRequest) throws IOException {
        Request request = new Request(HttpPost.METHOD_NAME, "index/shard/translog/apply");
        request.setEntity(createEntity(applyTranslogRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
    }

    static Request createIndex(CreateIndexRequest createIndexRequest) throws IOException {
        Request request = new Request(HttpPost.METHOD_NAME, "index/create");
        request.setEntity(createEntity(createIndexRequest, REQUEST_BODY_CONTENT_TYPE));
        return request;
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
}
