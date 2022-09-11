package com.phonepe.plaftorm.es.replicator.changes.plugin.rest;

import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesRequest;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.rest.*;

import java.io.IOException;

@Slf4j
public class ApplyShardHistoryOperations extends BaseRestHandler {
    public ApplyShardHistoryOperations(final Settings settings, final RestController controller) {
        super(settings);
        controller.registerHandler(RestRequest.Method.GET, "/_translog/operations", this);
    }

    @Override
    public String getName() {
        return ApplyShardHistoryOperations.class.getName();
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient nodeClient) throws IOException {
        String indexName = request.param("indexName");
        String indexUUID = request.param("indexUUID");
        int shard = request.paramAsInt("shardId", -1);
        int fromSeqNo = request.paramAsInt("fromSeqNo", 0);
        ShardId shardId = new ShardId(indexName, indexUUID, shard);



        return channel -> {
            channel.request().content();
//            XContentParser parser = JsonXContentParser.
            GetChangesRequest getChangesRequest = new GetChangesRequest(shardId, fromSeqNo, 0);
            log.info("Sending replication request {}", getChangesRequest);

//            ActionFuture<GetChangesResponse> responseActionFuture = nodeClient.execute(GetChangesAction.INSTANCE, getChangesRequest);
//            GetChangesResponse response = responseActionFuture.get();
//
//            XContentBuilder builder = XContentFactory.jsonBuilder();
//            builder.startObject();
////                    builder.field("currentNodeId", nodeClient.getLocalNodeId());
//            builder.field("changes", response.getChanges());
////                    builder.field("shardRoutings", shardRoutings);
//            builder.endObject();
//            BytesRestResponse r = new BytesRestResponse(RestStatus.OK, builder);
//            channel.sendResponse(r);

            nodeClient.executeLocally(GetChangesAction.INSTANCE, getChangesRequest, new ActionListener<GetChangesResponse>() {
                @Override
                @SneakyThrows
                public void onResponse(GetChangesResponse getChangesResponse) {
                    log.info("Received response {}", getChangesResponse);
                    BytesStreamOutput changesStream = new BytesStreamOutput();
                    changesStream.writeCollection(getChangesResponse.getChanges(), Translog.Operation::writeOperation);


                    XContentBuilder builder = XContentFactory.jsonBuilder();
                    builder.startObject();
//                    builder.field("currentNodeId", nodeClient.getLocalNodeId());
                    builder.field("changes", changesStream.bytes());
//                    builder.field("shardRoutings", shardRoutings);
                    builder.endObject();
                    BytesRestResponse response = new BytesRestResponse(RestStatus.OK, builder);
                    channel.sendResponse(response);
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("Error ", e);
                    channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()));
                }
            });

        };
    }
}
