package com.phonepe.platform.es.connector.tasks;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.phonepe.plaftorm.es.replicator.commons.job.JobContext;
import com.phonepe.plaftorm.es.replicator.commons.job.JobResponseCombiner;
import com.phonepe.plaftorm.es.replicator.commons.job.PollingJob;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.connector.models.ShardReplicateRequest;
import com.phonepe.platform.es.connector.store.ShardCheckpoint;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.models.EsShardRouting;
import com.phonepe.platform.es.replicator.models.GetChangesRequest;
import com.phonepe.platform.es.replicator.models.EsGetChangesResponse;
import com.phonepe.platform.es.replicator.models.SerializedTranslog;
import com.phonepe.platform.es.replicator.models.changes.ChangeEvent;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.atomic.AtomicReference;


@Slf4j
public class ShardReplicationTask extends PollingJob<Boolean> {
    private final ShardReplicateRequest shardReplicateRequest;

    private final TranslogCheckpointStore translogCheckpointStore;

    private final ESClient esClient;


    private final EventQueue<ChangeEvent> eventQueue;

    private final AtomicReference<ShardCheckpoint> lastCheckpoint = new AtomicReference<>();

    @Inject
    @Builder
    public ShardReplicationTask(@Assisted final ShardReplicateRequest shardReplicateRequest,
                                final TranslogCheckpointStore translogCheckpointStore,
                                final ESClient esClient,
                                final EventQueue<ChangeEvent> eventQueue) {
        this.shardReplicateRequest = shardReplicateRequest;
        this.translogCheckpointStore = translogCheckpointStore;
        this.esClient = esClient;
        this.eventQueue = eventQueue;
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
    @SneakyThrows
    public Boolean poll(final JobContext<Boolean> context, final JobResponseCombiner<Boolean> responseCombiner) {
        EsShardRouting shardRouting = shardReplicateRequest.getShardRouting();

        GetChangesRequest request = GetChangesRequest.builder()
                .shardId(shardRouting.getShardId())
                .indexName(shardRouting.getIndexName())
                .indexUUID(shardReplicateRequest.getIndexMetadata().getIndexUUID())
                .fromSeqNo(getStartSequence())
                .toSeqNo(getStartSequence() + 10)
                .build();

        EsGetChangesResponse response = esClient.getShardChanges(request);

        if (response == null) {
            return false;
        }
        log.info("Received {} changes for {}", response.getChanges().size(), jobId());
        if (!response.getChanges().isEmpty()) {


            SerializedTranslog translog = response.getChanges().get(response.getChanges().size() - 1);


            ChangeEvent changeEvent = ChangeEvent.builder()
                    .translog(translog)
                    .connectorSentTimestamp(System.currentTimeMillis())
                    .build();


            try {
                eventQueue.write(changeEvent);
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
