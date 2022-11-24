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
public class GetChangesRequest implements ToXContentObject {
    String indexName;
    String indexUUID;
    int shardId;

    long fromSeqNo;
    long toSeqNo;

    @Override
    public XContentBuilder toXContent(final XContentBuilder xContentBuilder, final Params params) throws IOException {
        return null;
    }
}
