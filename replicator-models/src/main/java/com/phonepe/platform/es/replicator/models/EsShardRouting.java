package com.phonepe.platform.es.replicator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ObjectParser;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;

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

    private static final String STATE_FIELD = "state";
    private static final String PRIMARY_FIELD = "primary";
    private static final String NODE_ID_FIELD = "nodeId";
    private static final String SHARD_ID_FIELD = "shardId";
    private static final String INDEX_NAME_FIELD = "indexName";
    private static final String ACTIVE_FIELD = "active";
    private static final String ASSIGNED_TO_NODE = "assignedToNode";

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field(STATE_FIELD, state);
        builder.field(PRIMARY_FIELD, primary);
        builder.field(NODE_ID_FIELD, nodeId);
        builder.field(SHARD_ID_FIELD, shardId);
        builder.field(INDEX_NAME_FIELD, indexName);
        builder.field(ACTIVE_FIELD, active);
        builder.field(ASSIGNED_TO_NODE, assignedToNode);
        builder.endObject();
        return builder;
    }

    public static final ObjectParser<EsShardRouting, Void> PARSER =
            new ObjectParser<>(
                    "esShardRouting",
                    false,
                    EsShardRouting::new
            );

    static {
        PARSER.declareString(EsShardRouting::setState, new ParseField(STATE_FIELD));
        PARSER.declareBoolean(EsShardRouting::setPrimary, new ParseField(PRIMARY_FIELD));
        PARSER.declareString(EsShardRouting::setNodeId, new ParseField(NODE_ID_FIELD));
        PARSER.declareInt(EsShardRouting::setShardId, new ParseField(SHARD_ID_FIELD));
        PARSER.declareStringOrNull(EsShardRouting::setIndexName, new ParseField(INDEX_NAME_FIELD));
        PARSER.declareBoolean(EsShardRouting::setActive, new ParseField(ACTIVE_FIELD));
        PARSER.declareBoolean(EsShardRouting::setAssignedToNode, new ParseField(ASSIGNED_TO_NODE));
    }
    public static EsShardRouting fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}
