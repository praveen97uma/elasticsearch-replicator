package com.phonepe.platform.es.replicator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsGetChangesResponse implements ToXContentObject {
    List<SerializedTranslog> changes;
    long fromSeqNo;
    long maxSeqNoOfUpdatesOrDeletes;
    long lastSyncedGlobalCheckpoint;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("changes", changes);
        builder.field("fromSeqNo", fromSeqNo);
        builder.field("maxSeqNoOfUpdatesOrDeletes", maxSeqNoOfUpdatesOrDeletes);
        builder.field("lastSyncedGlobalCheckpoint", lastSyncedGlobalCheckpoint);
        builder.endObject();
        return builder;
    }
}
