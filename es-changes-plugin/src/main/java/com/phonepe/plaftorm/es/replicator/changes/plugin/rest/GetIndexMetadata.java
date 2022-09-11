package com.phonepe.plaftorm.es.replicator.changes.plugin.rest;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GetIndexMetadata extends BaseRestHandler {
    public GetIndexMetadata(final Settings settings, final RestController controller) {
        super(settings);
        controller.registerHandler(RestRequest.Method.GET, "/_index/metadata", this);
    }

    @Override
    public String getName() {
        return GetIndexMetadata.class.getName();
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient nodeClient) throws IOException {
        String indexName = request.param("indexName");
        logger.info("Sending replication request {}", request);

        return channel -> {
            ImmutableOpenMap<String, IndexMetaData> indices = nodeClient.admin()
                    .cluster()
                    .prepareState()
                    .get()
                    .getState()
                    .getMetaData()
                    .getIndices();

            List<ShardRouting> shardRoutings = new ArrayList<>(nodeClient.admin()
                    .cluster()
                    .prepareState()
                    .get()
                    .getState()
                    .routingTable()
                    .allShards());


            class ShardMetaData {

            }


            List<IndexData> metaDatas = new ArrayList<>();
            indices.values().forEach((Consumer<ObjectCursor<IndexMetaData>>) indexMetaDataObjectCursor -> {
                IndexMetaData data = indexMetaDataObjectCursor.value;
                IndexData idata = IndexData.builder()
                        .indexName(data.getIndex().getName())
                        .indexUUID(data.getIndex().getUUID())
                        .mappingVersion(data.getMappingVersion())
                        .noOfReplicas(data.getNumberOfReplicas())
                        .noOfShards(data.getNumberOfShards())
                        .state(data.getState())
                        .build();
                metaDatas.add(idata);
            });

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("currentNodeId", nodeClient.getLocalNodeId());
            builder.field("indices", metaDatas);
            builder.field("shardRoutings", shardRoutings);
            builder.endObject();
            BytesRestResponse response = new BytesRestResponse(RestStatus.OK, builder);
            channel.sendResponse(response);
        };
    }
}
