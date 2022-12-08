package com.phonepe.platform.es.replicator.models;

import junit.framework.TestCase;
import org.elasticsearch.cluster.ClusterModule;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.*;

import java.io.IOException;
import java.util.List;

public class CreateIndexRequestTest extends TestCase {

    public void testToXContent() throws IOException {
        CreateIndexRequest request = CreateIndexRequest.builder()
                .esIndexMetadata(EsIndexMetadata.builder()
                        .mappingType("doc")
                        .indexName("test")
                        .indexUUID("testUUID")
                        .state("STARTING")
                        .noOfReplicas(2)
                        .noOfShards(2)
                        .mapping("mappingBytes")
                        .shardRoutings(List.of(EsShardRouting.builder()
                                        .active(true)
                                        .primary(false)
                                        .assignedToNode(true)
                                        .state("RUNNING")
                                        .nodeId("node123")
                                .build()))
                        .mappingVersion(1)
                        .aliases(List.of(EsAliasMetadata.builder()
                                        .alias(null)
                                        .indexRouting("rot")
                                        .isHidden(false)
                                        .build()))
                        .settingsBytes("sdfsdfs")
                        .build())
                .build();

        XContentBuilder builder = XContentFactory.jsonBuilder();
        request.toXContent(builder, ToXContent.EMPTY_PARAMS);

        String p = Strings.toString(builder);

        BytesReference data = BytesReference.bytes(builder);

        XContent xContent = XContentFactory.xContent(XContentType.JSON);
        XContentParser parser = createParser(xContent, data);

        CreateIndexRequest deser = CreateIndexRequest.fromXContent(parser);
        XContentBuilder builder2 = XContentFactory.jsonBuilder();
        deser.toXContent(builder2, ToXContent.EMPTY_PARAMS);
        String q = Strings.toString(builder2);
        assert request.equals(deser);
    }

    protected final XContentParser createParser(XContent xContent, BytesReference data) throws IOException {
        return xContent.createParser(this.xContentRegistry(), LoggingDeprecationHandler.INSTANCE, data.streamInput());
    }

    protected NamedXContentRegistry xContentRegistry() {
        return new NamedXContentRegistry(ClusterModule.getNamedXWriteables());
    }
}