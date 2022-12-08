package com.phonepe.platform.es.replicator.models;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.cluster.ClusterModule;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ApplyTranslogRequestTest {

    @Test
    public void testSerDe() throws IOException {
        SerializedTranslog translog = SerializedTranslog.builder()
                .seqNo(10)
                .translogBytes("test")
                .opType("INDEX")
                .build();

        ApplyTranslogRequest req = new ApplyTranslogRequest("test-index", 1, List.of(translog), 1);

        XContentBuilder builder = XContentFactory.jsonBuilder();
        req.toXContent(builder, ToXContent.EMPTY_PARAMS);

        String p = Strings.toString(builder);

        BytesReference data = BytesReference.bytes(builder);

        XContent xContent = XContentFactory.xContent(XContentType.JSON);
        XContentParser parser = createParser(xContent, data);

        ApplyTranslogRequest deser = ApplyTranslogRequest.fromXContent(parser);
        XContentBuilder builder2 = XContentFactory.jsonBuilder();
        deser.toXContent(builder2, ToXContent.EMPTY_PARAMS);
        String q = Strings.toString(builder2);
        assert req.equals(deser);

    }


    protected final XContentParser createParser(XContent xContent, BytesReference data) throws IOException {
        return xContent.createParser(this.xContentRegistry(), LoggingDeprecationHandler.INSTANCE, data.streamInput());
    }

    protected NamedXContentRegistry xContentRegistry() {
        return new NamedXContentRegistry(ClusterModule.getNamedXWriteables());
    }
}