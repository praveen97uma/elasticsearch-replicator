package com.phonepe.platform.es.replicator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.util.ByteArray;
import org.elasticsearch.common.xcontent.*;

import java.io.IOException;
import java.util.Base64;

import static org.elasticsearch.common.xcontent.ConstructingObjectParser.constructorArg;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerializedTranslog implements ToXContentObject, Writeable, Writeable.Reader<SerializedTranslog> {
    private static final String TRANSLOG_BYTES_FIELD = "translogBytes";
    private static final String SEQNO_FIELD = "seqNo";
    private static final String PRIMARY_TERM_FIELD = "primaryTerm";
    private static final String OPTYPE_FIELD = "opType";
    String translogBytes;
    long seqNo;

    long primaryTerm;

    String opType;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("translogBytes", translogBytes);
        builder.field("seqNo", seqNo);
        builder.field("opType", opType);
        builder.field("primaryTerm", primaryTerm);
        builder.endObject();
        return builder;
    }

    @Override
    public void writeTo(final StreamOutput streamOutput) throws IOException {
        streamOutput.writeLong(seqNo);
        streamOutput.writeString(opType);
        streamOutput.writeString(translogBytes);
    }

    @Override
    public SerializedTranslog read(final StreamInput streamInput) throws IOException {
        this.setSeqNo(streamInput.readLong());
        this.setOpType(streamInput.readString());
        this.setTranslogBytes(streamInput.readString());
        return this;
    }

    public static SerializedTranslog readFrom(final StreamInput input) throws IOException {
        SerializedTranslog translog = new SerializedTranslog();
        translog.read(input);
        return translog;
    }

    static final ObjectParser<SerializedTranslog, Void> PARSER =
            new ObjectParser<>(
                    "serializedtranslog",
                    false,
                    SerializedTranslog::new
            );

    static {
        PARSER.declareString(SerializedTranslog::setTranslogBytes, new ParseField(TRANSLOG_BYTES_FIELD));
        PARSER.declareLong(SerializedTranslog::setSeqNo, new ParseField(SEQNO_FIELD));
        PARSER.declareLong(SerializedTranslog::setPrimaryTerm, new ParseField(PRIMARY_TERM_FIELD));
        PARSER.declareString(SerializedTranslog::setOpType, new ParseField(OPTYPE_FIELD));

    }

    public static SerializedTranslog fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}
