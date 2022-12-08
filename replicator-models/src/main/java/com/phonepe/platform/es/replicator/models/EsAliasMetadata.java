package com.phonepe.platform.es.replicator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ObjectParser;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsAliasMetadata implements ToXContentObject {
    String alias;
    String indexRouting;
    String searchRouting;
    boolean writeIndex;
    boolean isHidden;

    private static final String ALIAS_FIELD = "alias";
    private static final String INDEX_ROUTING_FIELD = "indexRouting";
    private static final String SEARCH_ROUTING_FIELD = "searchRouting";
    private static final String WRITE_INDEX_FIELD = "writeIndex";
    private static final String IS_HIDDEN_FIELD = "isHidden";

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field(ALIAS_FIELD, alias);
        builder.field(INDEX_ROUTING_FIELD, indexRouting);
        builder.field(SEARCH_ROUTING_FIELD, searchRouting);
        builder.field(WRITE_INDEX_FIELD, writeIndex);
        builder.field(IS_HIDDEN_FIELD, isHidden);
        builder.endObject();
        return builder;
    }

    public static final ObjectParser<EsAliasMetadata, Void> PARSER =
            new ObjectParser<>(
                    "esAliasMetadata",
                    false,
                    EsAliasMetadata::new
            );

    static {
        PARSER.declareStringOrNull(EsAliasMetadata::setAlias, new ParseField(ALIAS_FIELD));
        PARSER.declareStringOrNull(EsAliasMetadata::setIndexRouting, new ParseField(INDEX_ROUTING_FIELD));
        PARSER.declareStringOrNull(EsAliasMetadata::setSearchRouting, new ParseField(SEARCH_ROUTING_FIELD));
        PARSER.declareBoolean(EsAliasMetadata::setWriteIndex, new ParseField(WRITE_INDEX_FIELD));
        PARSER.declareBoolean(EsAliasMetadata::setHidden, new ParseField(IS_HIDDEN_FIELD));
    }

    public static EsAliasMetadata fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}
