package com.phonepe.platform.es.replicator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerializedTranslog implements ToXContentObject {
    byte[] translogBytes;
    long seqNo;
    String opType;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("translogBytes", translogBytes);
        builder.field("seqNo", seqNo);
        builder.field("opType", opType);
        builder.endObject();
        return builder;
    }
}
