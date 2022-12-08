package com.phonepe.platform.es.replicator.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetIndexAndShardsMetadataResponse implements ToXContentObject {
    String currentNodeId;

    List<EsIndexMetadata> indexMetadatas;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("currentNodeId", currentNodeId);
        builder.field("indexMetadatas", indexMetadatas);
        builder.endObject();
        return builder;
    }
}
