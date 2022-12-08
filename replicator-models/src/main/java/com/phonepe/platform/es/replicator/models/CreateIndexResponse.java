package com.phonepe.platform.es.replicator.models;

import lombok.*;
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
@ToString
public class CreateIndexResponse implements ToXContentObject {
    private boolean success;

    private static final String SUCCESS_FIELD = "success";

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field(SUCCESS_FIELD, success);
        builder.endObject();
        return builder;
    }

    private static final ObjectParser<CreateIndexResponse, Void> PARSER =
            new ObjectParser<>(
                    CreateIndexResponse.class.getSimpleName(),
                    false,
                    CreateIndexResponse::new
            );

    static {
        PARSER.declareBoolean(CreateIndexResponse::setSuccess,  new ParseField(SUCCESS_FIELD));
    }

    public static CreateIndexResponse fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}
