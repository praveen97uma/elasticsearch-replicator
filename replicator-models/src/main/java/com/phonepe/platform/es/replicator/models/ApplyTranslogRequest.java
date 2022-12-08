package com.phonepe.platform.es.replicator.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.elasticsearch.client.Validatable;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.xcontent.*;

import java.io.IOException;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder
public class ApplyTranslogRequest implements Writeable, Writeable.Reader<ApplyTranslogRequest>, ToXContentObject, Validatable {
    private static final String INDEXNAME_FIELD = "indexName";
    private static final String SHARDID_FIELD = "shardId";
    private static final String SERIALIZED_TRANSLOG_FIELD = "serializedTranslog";

    private static final String MSU_FIELD = "maxSeqNoOfUpdatesOrDeletes";


    String indexName;
    int shardId;
    List<SerializedTranslog> serializedTranslog;

    long maxSeqNoOfUpdatesOrDeletes;


    private static final ObjectParser<ApplyTranslogRequest, Void> PARSER =
            new ObjectParser<>(
                    "applytranslogrequest",
                    false, ApplyTranslogRequest::new
            );

    static {
        PARSER.declareString(ApplyTranslogRequest::setIndexName, new ParseField(INDEXNAME_FIELD));
        PARSER.declareInt(ApplyTranslogRequest::setShardId, new ParseField(SHARDID_FIELD));
        PARSER.declareObjectArray(ApplyTranslogRequest::setSerializedTranslog, SerializedTranslog.PARSER  ,new ParseField(SERIALIZED_TRANSLOG_FIELD));
        PARSER.declareLong(ApplyTranslogRequest::setMaxSeqNoOfUpdatesOrDeletes, new ParseField(MSU_FIELD));
    }


    @Override
    public void writeTo(final StreamOutput streamOutput) throws IOException {
        streamOutput.writeString(indexName);
        streamOutput.writeInt(shardId);
        streamOutput.writeList(serializedTranslog);
        streamOutput.writeLong(maxSeqNoOfUpdatesOrDeletes);
    }

    @Override
    public ApplyTranslogRequest read(final StreamInput streamInput) throws IOException {
        this.setIndexName(streamInput.readString());
        this.setShardId(streamInput.readInt());
        this.setSerializedTranslog(streamInput.readList(SerializedTranslog::readFrom));
        this.setMaxSeqNoOfUpdatesOrDeletes(streamInput.readLong());
        return this;
    }

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("indexName", indexName);
        builder.field("shardId", shardId);
        builder.field("serializedTranslog", serializedTranslog);
        builder.field("maxSeqNoOfUpdatesOrDeletes", maxSeqNoOfUpdatesOrDeletes);
        builder.endObject();
        return builder;
    }

    public static ApplyTranslogRequest fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }
}
