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
public class EsAliasMetadata implements ToXContentObject {
    String alias;
    String indexRouting;
    String searchRouting;
    boolean writeIndex;
    boolean isHidden;

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        builder.field("alias", alias);
        builder.field("indexRouting", indexRouting);
        builder.field("searchRouting", searchRouting);
        builder.field("writeIndex", writeIndex);
        builder.field("isHidden", isHidden);
        builder.endObject();
        return builder;
    }
}
