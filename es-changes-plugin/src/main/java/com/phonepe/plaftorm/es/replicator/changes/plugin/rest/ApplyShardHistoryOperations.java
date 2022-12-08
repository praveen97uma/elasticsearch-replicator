package com.phonepe.plaftorm.es.replicator.changes.plugin.rest;

import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay.ReplayChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay.ReplayChangesReq;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay.ReplayChangesResponse;
import com.phonepe.platform.es.replicator.models.ApplyTranslogRequest;
import com.phonepe.platform.es.replicator.models.ApplyTranslogResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.io.stream.ByteBufferStreamInput;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.phonepe.plaftorm.es.replicator.changes.plugin.injection.Dependencies.clusterService;

@Slf4j
public class ApplyShardHistoryOperations extends BaseRestHandler {
    public ApplyShardHistoryOperations(final Settings settings, final RestController controller) {
        super(settings);
        controller.registerHandler(RestRequest.Method.POST, "/index/shard/translog/apply", this);
    }

    @Override
    public String getName() {
        return ApplyShardHistoryOperations.class.getName();
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient nodeClient) throws IOException {

        // TODO: handle non existence of targetIndex

        // index uuid should be of the index in the replica cluster



        return channel -> {
            ApplyTranslogRequest applyTranslogRequest = ApplyTranslogRequest.fromXContent(request.contentParser());

            log.info("Got apply changes request for indexName: {}, ShardId: {}", applyTranslogRequest.getIndexName(),
                    applyTranslogRequest.getShardId());

            IndexMetaData targetIndex = clusterService.state().getMetaData()
                    .index(applyTranslogRequest.getIndexName());

            if (targetIndex == null) {
                ApplyTranslogResponse applyTranslogResponse = ApplyTranslogResponse.builder()
                        .success(false)
                        .errorCode(ApplyTranslogResponse.ErrorCode.INDEX_DOES_NOT_EXISTS)
                        .sequence(-1)
                        .build();

                XContentBuilder builder = XContentFactory.jsonBuilder();

                BytesRestResponse response = new BytesRestResponse(RestStatus.OK,
                        applyTranslogResponse.toXContent(builder, ToXContent.EMPTY_PARAMS));
                channel.sendResponse(response);
                return;
            }

            ShardId shardId = new ShardId(applyTranslogRequest.getIndexName(), targetIndex.getIndexUUID(), applyTranslogRequest.getShardId());

            List<Translog.Operation> ops = translateToTranslogOp(applyTranslogRequest);

            ReplayChangesReq applyChangesRequest = new ReplayChangesReq(
                    shardId,
                    applyTranslogRequest.getIndexName(),
                    ops,
                    applyTranslogRequest.getMaxSeqNoOfUpdatesOrDeletes());

            nodeClient.execute(ReplayChangesAction.INSTANCE, applyChangesRequest, new ActionListener<>() {
                @Override
                @SneakyThrows
                public void onResponse(ReplayChangesResponse applyChangesResponse) {
                    log.info("Received response {}", applyChangesResponse);

                    ApplyTranslogResponse applyTranslogResponse = ApplyTranslogResponse.builder()
                            .success(applyChangesResponse.isSuccess())
                            .sequence(applyChangesResponse.getLastSuccessfulSeqNo())
                            .errorCode(applyChangesResponse.getErrorCode())
                            .build();

                    XContentBuilder builder = XContentFactory.jsonBuilder();

                    BytesRestResponse response = new BytesRestResponse(RestStatus.OK,
                            applyTranslogResponse.toXContent(builder, ToXContent.EMPTY_PARAMS));
                    channel.sendResponse(response);
                }

                @SneakyThrows
                @Override
                public void onFailure(Exception e) {
                    log.error("Error ", e);
                    ApplyTranslogResponse applyTranslogResponse = ApplyTranslogResponse.builder()
                            .success(false)
                            .errorMessage(e.getLocalizedMessage())
                            .build();
                    XContentBuilder builder = XContentFactory.jsonBuilder();
                    channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR,
                            applyTranslogResponse.toXContent(builder, ToXContent.EMPTY_PARAMS)));
                }
            });

        };
    }

    private static List<Translog.Operation> translateToTranslogOp(final ApplyTranslogRequest applyTranslogRequest) {
        return applyTranslogRequest.getSerializedTranslog().stream().map(translog -> {
            BytesArray array = new BytesArray(Base64.getDecoder().decode(translog.getTranslogBytes()));
            StreamInput streamInput = new ByteBufferStreamInput(ByteBuffer.wrap(array.array()));
            try {
                return Translog.Operation.readOperation(streamInput);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
