package com.phonepe.platform.es.replicator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class EsShardRouting implements ToXContentObject {
    private String state;
    private boolean primary;
    private String nodeId;
    private int shardId;

    private String indexName;

    private boolean active ;

    private boolean assignedToNode;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("state", state);
        builder.field("primary", primary);
        builder.field("nodeId", nodeId);
        builder.field("shardId", shardId);
        builder.field("indexName", indexName);
        builder.field("active", active);
        builder.field("assignedToNode", assignedToNode);
        builder.endObject();
        return builder;
    }
}
