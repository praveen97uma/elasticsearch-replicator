package com.phonepe.platform.es.connector.tasks;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.phonepe.plaftorm.es.replicator.commons.job.JobContext;
import com.phonepe.plaftorm.es.replicator.commons.job.JobResponseCombiner;
import com.phonepe.plaftorm.es.replicator.commons.job.PollingJob;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.connector.models.ShardReplicateRequest;
import com.phonepe.platform.es.connector.sink.Sink;
import com.phonepe.platform.es.connector.store.ShardCheckpoint;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.grpc.events.Events;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.atomic.AtomicReference;

import static com.phonepe.platform.es.replicator.grpc.Engine.*;

@Slf4j
public class ShardReplicationTask extends PollingJob<Boolean> {
    private final ShardReplicateRequest shardReplicateRequest;

    private final TranslogCheckpointStore translogCheckpointStore;

    private final ESClient esClient;


    private final Sink<Events.ChangeEvent> sink;

    private final AtomicReference<ShardCheckpoint> lastCheckpoint = new AtomicReference<>();

    @Inject
    @Builder
    public ShardReplicationTask(@Assisted final ShardReplicateRequest shardReplicateRequest,
                                final TranslogCheckpointStore translogCheckpointStore,
                                final ESClient esClient,
                                final Sink<Events.ChangeEvent> sink) {
        this.shardReplicateRequest = shardReplicateRequest;
        this.translogCheckpointStore = translogCheckpointStore;
        this.esClient = esClient;
        this.sink = sink;
    }

    @Override
    public String jobId() {
        return shardReplicateRequest.getShardId();
    }

    @Override
    public void init() {
        translogCheckpointStore.getCheckpoint(
                shardReplicateRequest.getShardRouting().getIndexName(),
                shardReplicateRequest.getShardRouting().getShardId())
                .ifPresent(checkpoint -> {
                    log.info("Setting start checkpoint seq for {} as {}", jobId(), checkpoint.getSequence());
                    lastCheckpoint.set(checkpoint);
                });
    }

    @Override
    public Boolean poll(final JobContext<Boolean> context, final JobResponseCombiner<Boolean> responseCombiner) {
        ESShardRouting shardRouting = shardReplicateRequest.getShardRouting();

        GetChangesRequest request = GetChangesRequest.newBuilder()
                .setShardId(shardRouting.getShardId())
                .setIndexName(shardRouting.getIndexName())
                .setIndexUUID(shardReplicateRequest.getIndexMetadata().getIndexUUID())
                .setFromSeqNo(getStartSequence())
                .setToSeqNo(getStartSequence() + 10)
                .build();

        GetChangesResponse response = esClient.getShardChanges(request);
        log.info("Received {} changes for {}", response.getChangesCount(), jobId());
        if (!response.getChangesList().isEmpty()) {


            SerializedTranslog translog = response.getChangesList().get(response.getChangesList().size() - 1);


            Events.ChangeEvent changeEvent = Events.ChangeEvent.newBuilder()
                    .setTranslog(translog)
                    .setConnectorSentTimestamp(System.currentTimeMillis())
                    .build();


            try {
                sink.write(changeEvent);
            } catch (Exception e) {
                log.error("Error writing to sink", e);
            }

            val checkpoint = ShardCheckpoint.builder()
                    .shardId(shardRouting.getShardId())
                    .indexName(shardRouting.getIndexName())
                    .timestamp(System.currentTimeMillis())
                    .sequence(translog.getSeqNo())
                    .build();

            lastCheckpoint.set(checkpoint);
            translogCheckpointStore.saveCheckpoint(checkpoint);

            log.info("Setting checkpoint for {} as {}", jobId(), checkpoint.getSequence());
        }

        return true;
    }

    private long getStartSequence() {
        return lastCheckpoint.get() == null ? 0 : lastCheckpoint.get().getSequence() + 1;
    }
}
