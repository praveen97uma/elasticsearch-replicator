package com.phonepe.platform.es.replicator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.*;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.common.xcontent.ConstructingObjectParser.optionalConstructorArg;

/*
message ApplyTranslogResponse {
  bool success = 1;
  string error = 2;
  ErrorCode errorCode = 3;
  int64 sequence = 4;

  enum ErrorCode {
    DEFAULT = 0;
    INDEX_DOES_NOT_EXISTS = 1;
    MAPPING_UPDATE_REQUIRED = 2;
  }
}
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ApplyTranslogResponse implements ToXContentObject {
    private static final String SUCCESS_FIELD = "success";
    private static final String ERROR_MESSAGE_FIELD = "errorMessage";
    private static final String ERROR_CODE_FIELD = "errorCode";
    private static final String SEQUENCE_FIELD = "sequence";

    boolean success;
    String errorMessage;
    ErrorCode errorCode;
    long sequence;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field(SUCCESS_FIELD, success);
        builder.field(ERROR_MESSAGE_FIELD, errorMessage);
        builder.field(ERROR_CODE_FIELD, errorCode);
        builder.field(SEQUENCE_FIELD, sequence);
        builder.endObject();
        return builder;
    }

    public enum ErrorCode {
        INDEX_DOES_NOT_EXISTS,
        MAPPING_UPDATE_REQUIRED,

        UNKNOWN
    }

    private static final ObjectParser<ApplyTranslogResponse, Void> PARSER =
            new ObjectParser<>(
                    "applytranslogresponse",
                    false,
                    ApplyTranslogResponse::new
            );

    static {
        PARSER.declareBoolean(ApplyTranslogResponse::setSuccess, new ParseField(SUCCESS_FIELD));
        PARSER.declareStringOrNull(ApplyTranslogResponse::setErrorMessage, new ParseField(ERROR_MESSAGE_FIELD));
        PARSER.declareStringOrNull((x, y) -> {
            if (y != null) x.setErrorCode(ErrorCode.valueOf(y));
        }, new ParseField(ERROR_CODE_FIELD));
        PARSER.declareInt(ApplyTranslogResponse::setSequence, new ParseField(SEQUENCE_FIELD));
    }

    public static ApplyTranslogResponse fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}

