package com.phonepe.plaftorm.es.replicator.changes.plugin.rest;

import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesRequest;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesResponse;
import com.phonepe.platform.es.replicator.models.EsGetChangesResponse;
import com.phonepe.platform.es.replicator.models.SerializedTranslog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
public class GetShardChanges extends BaseRestHandler {
    public GetShardChanges(final Settings settings, final RestController controller) {
        super(settings);
        controller.registerHandler(RestRequest.Method.GET, "/index/shard/changes", this);
    }

    @Override
    public String getName() {
        return GetShardChanges.class.getName();
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient nodeClient) throws IOException {

        String indexName = request.param("indexName");
        String indexUUID = request.param("indexUUID");
        int shard = request.paramAsInt("shardId", -1);
        int fromSeqNo = request.paramAsInt("fromSeqNo", 0);
        ShardId shardId = new ShardId(indexName, indexUUID, shard);



        return channel -> {
            GetChangesRequest getChangesRequest = new GetChangesRequest(shardId, fromSeqNo, 0);
            log.info("Sending replication request {}", getChangesRequest);

            nodeClient.executeLocally(GetChangesAction.INSTANCE, getChangesRequest, new ActionListener<GetChangesResponse>() {
                @Override
                @SneakyThrows
                public void onResponse(GetChangesResponse getChangesResponse) {

                    EsGetChangesResponse response = EsGetChangesResponse.builder()
                            .changes(getChangesResponse.getChanges().stream().map(GetShardChanges::translate).collect(Collectors.toList()))
                            .fromSeqNo(getChangesRequest.getFromSeqNo())
                            .lastSyncedGlobalCheckpoint(getChangesResponse.getLastSyncedGlobalCheckpoint())
                            .maxSeqNoOfUpdatesOrDeletes(getChangesResponse.getMaxSeqNoOfUpdatesOrDeletes())
                            .fromSeqNo(getChangesResponse.getFromSeqNo())
                            .build();

                    XContentBuilder builder = XContentFactory.jsonBuilder();
                    channel.sendResponse(new BytesRestResponse(RestStatus.OK, response.toXContent(builder, ToXContent.EMPTY_PARAMS)));
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("Error ", e);
                    channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()));
                }
            });

        };
    }

    private static SerializedTranslog translate(final Translog.Operation change) {
        BytesStreamOutput streamOutput = new BytesStreamOutput();
        try {
            Translog.Operation.writeOperation(streamOutput, change);
            return SerializedTranslog.builder()
                    .translogBytes(streamOutput.bytes().toBytesRef().bytes)
                    .seqNo(change.seqNo())
                    .opType(change.opType().name())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
