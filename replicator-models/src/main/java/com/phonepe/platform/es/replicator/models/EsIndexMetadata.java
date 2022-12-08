package com.phonepe.platform.es.replicator.models;

import lombok.*;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ObjectParser;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EsIndexMetadata implements ToXContentObject {

    String indexName;
    String indexUUID;
    long mappingVersion;
    long noOfReplicas;
    long noOfShards;
    String state;
    String mapping;
    List<EsAliasMetadata> aliases;

    String settingsBytes;
    String mappingType;
    List<EsShardRouting> shardRoutings;

    private static final String INDEXNAME_FIELD = "indexName";
    private static final String INDEXUUID_FIELD = "indexUUID";
    private static final String MAPPING_VERSION_FIELD = "mappingVersion";
    private static final String NO_OF_REPLICAS_FIELD = "noOfReplicas";
    private static final String NO_OF_SHARDS_FIELD = "noOfShards";
    private static final String STATE_FIELD = "state";
    private static final String MAPPING_FIELD = "mapping";
    private static final String ALIASES_FIELD = "aliases";
    private static final String SETTINGS_FIELD = "settingsBytes";
    private static final String MAPPING_TYPE_FIELD = "mappingType";
    private static final String SHARD_ROUTINGS_FIELD = "shardRoutings";

    public static final ObjectParser<EsIndexMetadata, Void> PARSER =
            new ObjectParser<>(
                    "esIndexMetadata",
                    false,
                    EsIndexMetadata::new
            );

    static {
        PARSER.declareString(EsIndexMetadata::setIndexName, new ParseField(INDEXNAME_FIELD));
        PARSER.declareStringOrNull(EsIndexMetadata::setIndexUUID, new ParseField(INDEXUUID_FIELD));
        PARSER.declareLong(EsIndexMetadata::setMappingVersion, new ParseField(MAPPING_VERSION_FIELD));
        PARSER.declareLong(EsIndexMetadata::setNoOfReplicas, new ParseField(NO_OF_REPLICAS_FIELD));
        PARSER.declareLong(EsIndexMetadata::setNoOfShards, new ParseField(NO_OF_SHARDS_FIELD));
        PARSER.declareString(EsIndexMetadata::setState, new ParseField(STATE_FIELD));
        PARSER.declareStringOrNull(EsIndexMetadata::setMapping, new ParseField(MAPPING_FIELD));
        PARSER.declareObjectArray(EsIndexMetadata::setAliases, EsAliasMetadata.PARSER, new ParseField(ALIASES_FIELD));
        PARSER.declareStringOrNull(EsIndexMetadata::setSettingsBytes, new ParseField(SETTINGS_FIELD));
        PARSER.declareStringOrNull(EsIndexMetadata::setMappingType, new ParseField(MAPPING_TYPE_FIELD));
        PARSER.declareObjectArray(EsIndexMetadata::setShardRoutings, EsShardRouting.PARSER,
                new ParseField(SHARD_ROUTINGS_FIELD));
    }

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field(INDEXNAME_FIELD, indexName);
        builder.field(INDEXUUID_FIELD, indexUUID);
        builder.field(MAPPING_VERSION_FIELD, mappingVersion);
        builder.field(NO_OF_REPLICAS_FIELD, noOfReplicas);
        builder.field(NO_OF_SHARDS_FIELD, noOfShards);
        builder.field(STATE_FIELD, state);
        builder.field(MAPPING_FIELD, mapping);
        builder.field(ALIASES_FIELD, aliases);
        builder.field(SETTINGS_FIELD, settingsBytes);
        builder.field(MAPPING_TYPE_FIELD, mappingType);
        builder.field(SHARD_ROUTINGS_FIELD, shardRoutings);
        builder.endObject();
        return builder;
    }

    public static EsIndexMetadata fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}
