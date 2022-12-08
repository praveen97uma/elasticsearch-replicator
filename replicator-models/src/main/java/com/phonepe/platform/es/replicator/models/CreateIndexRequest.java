package com.phonepe.platform.es.replicator.models;

import lombok.*;
import org.elasticsearch.client.Validatable;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.*;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateIndexRequest implements ToXContentObject, Validatable {
    private EsIndexMetadata esIndexMetadata;

    private static final String ES_INDEX_METADATA_FIELD = "esIndexMetadata";

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field(ES_INDEX_METADATA_FIELD, esIndexMetadata);
        builder.endObject();
        return builder;
    }

    private static final ObjectParser<CreateIndexRequest, Void> PARSER =
            new ObjectParser<>(
                    "createIndexRequest",
                    false,
                    CreateIndexRequest::new
            );

    static {
        PARSER.declareObject(CreateIndexRequest::setEsIndexMetadata, EsIndexMetadata.PARSER,  new ParseField(ES_INDEX_METADATA_FIELD));
    }

    public static CreateIndexRequest fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}
