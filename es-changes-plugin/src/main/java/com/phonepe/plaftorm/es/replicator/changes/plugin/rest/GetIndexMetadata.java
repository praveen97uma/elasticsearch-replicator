package com.phonepe.plaftorm.es.replicator.changes.plugin.rest;

import com.phonepe.platform.es.replicator.models.EsIndexMetadata;
import com.phonepe.platform.es.replicator.models.EsShardRouting;
import com.phonepe.platform.es.replicator.models.GetIndexAndShardsMetadataResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

            List<EsShardRouting> shardRoutings = new ArrayList<>(nodeClient.admin()
                    .cluster()
                    .prepareState()
                    .get()
                    .getState()
                    .routingTable()
                    .allShards()).stream()
                    .map(GetIndexMetadata::translateShardRouting)
                    .collect(Collectors.toList());

            Map<String, List<EsShardRouting>> routingsMap = shardRoutings.stream()
                    .collect(Collectors.groupingBy(EsShardRouting::getIndexName));




            List<EsIndexMetadata> indexMetadatas = new ArrayList<>();
            indices.valuesIt().forEachRemaining(indexMetaData -> {
                indexMetadatas.add(EsIndexMetadata.builder()
                        .indexName(indexMetaData.getIndex().getName())
                        .indexUUID(indexMetaData.getIndexUUID())
                        .noOfShards(indexMetaData.getNumberOfShards())
                        .noOfReplicas(indexMetaData.getNumberOfReplicas())
                        .state(indexMetaData.getState().name())
                        .mappingVersion(indexMetaData.getMappingVersion())
                        .mapping(indexMetaData.mapping().source().string())
//                        .settingsBytes(indexMetaData.getSettings().)
                        .mappingType(indexMetaData.mapping().type())
                        .shardRoutings(routingsMap.getOrDefault(indexMetaData.getIndex().getName(), List.of()))
                        .build());
            });

            XContentBuilder builder = XContentFactory.jsonBuilder();

            GetIndexAndShardsMetadataResponse res = GetIndexAndShardsMetadataResponse.builder()
                    .currentNodeId(nodeClient.getLocalNodeId())
                    .indexMetadatas(indexMetadatas)
                    .build();

            channel.sendResponse(
                    new BytesRestResponse(RestStatus.OK, res.toXContent(builder, ToXContent.EMPTY_PARAMS)));
        };
    }

    private static EsShardRouting translateShardRouting(final ShardRouting shardRouting) {
        return EsShardRouting.builder()
                .shardId(shardRouting.id())
                .indexName(shardRouting.getIndexName())
                .primary(shardRouting.primary())
                .state(shardRouting.state().name())
                .assignedToNode(shardRouting.assignedToNode())
                .active(shardRouting.active())
                .nodeId(shardRouting.currentNodeId())
                .build();
    }
}
