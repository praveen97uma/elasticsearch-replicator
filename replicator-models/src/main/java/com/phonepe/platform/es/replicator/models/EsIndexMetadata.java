package com.phonepe.platform.es.replicator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsIndexMetadata implements ToXContentObject {
    String indexName;
    String indexUUID;
    long mappingVersion;
    long noOfReplicas;
    long noOfShards;
    String state;
    byte[] mapping;
    List<EsAliasMetadata> aliases;
    byte[] settings;
    String mappingType;
    List<EsShardRouting> shardRoutings;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("indexName", indexName);
        builder.field("indexUUID", indexUUID);
        builder.field("mappingVersion", mappingVersion);
        builder.field("noOfReplicas", noOfReplicas);
        builder.field("noOfShards", noOfShards);
        builder.field("state", state);
        builder.field("mapping", mapping);
        builder.field("aliases", aliases);
        builder.field("settings", settings);
        builder.field("mappingType", mappingType);
        builder.field("shardRoutings", shardRoutings);
        builder.endObject();
        return builder;
    }
}
