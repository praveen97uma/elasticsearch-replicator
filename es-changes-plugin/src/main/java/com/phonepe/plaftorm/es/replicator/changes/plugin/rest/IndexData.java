package com.phonepe.plaftorm.es.replicator.changes.plugin.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndexData implements ToXContentObject {
    String indexName;
    String indexUUID;
    IndexMetaData.State state;
    long mappingVersion;
    int noOfShards;
    int noOfReplicas;

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field("indexName", indexName);
        builder.field("indexUUID", indexUUID);
        builder.field("state", state);
        builder.field("mappingVersion", mappingVersion);
        builder.field("noOfShards", noOfShards);
        builder.field("noOfReplicas", noOfReplicas);
        builder.endObject();
        return builder;
    }
}
