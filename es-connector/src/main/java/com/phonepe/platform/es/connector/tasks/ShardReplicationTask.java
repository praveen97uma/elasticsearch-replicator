package com.phonepe.platform.es.connector.tasks;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.phonepe.plaftorm.es.replicator.commons.job.JobContext;
import com.phonepe.plaftorm.es.replicator.commons.job.JobResponseCombiner;
import com.phonepe.plaftorm.es.replicator.commons.job.PollingJob;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.plaftorm.es.replicator.commons.queue.WriteResult;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.connector.models.ShardReplicateRequest;
import com.phonepe.platform.es.connector.store.ShardCheckpoint;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.models.*;
import com.phonepe.platform.es.replicator.models.changes.ChangeEvent;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.inject.Named;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
public class ShardReplicationTask extends PollingJob<Boolean> {
    private final ShardReplicateRequest shardReplicateRequest;

    private final TranslogCheckpointStore translogCheckpointStore;

    private final ESClient esClient;


    private final EventQueue<ChangeEvent> eventQueue;

    private final AtomicReference<ShardCheckpoint> lastCheckpoint = new AtomicReference<>();

    private final ESClient replicaESClient;

    @Inject
    @Builder
    public ShardReplicationTask(@Assisted final ShardReplicateRequest shardReplicateRequest,
                                final TranslogCheckpointStore translogCheckpointStore,
                                final @Named("source") ESClient esClient,
                                final EventQueue<ChangeEvent> eventQueue,
                                final @Named("replica") ESClient replicaESClient) {
        this.shardReplicateRequest = shardReplicateRequest;
        this.translogCheckpointStore = translogCheckpointStore;
        this.esClient = esClient;
        this.eventQueue = eventQueue;
        this.replicaESClient = replicaESClient;
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

        if (response == null || response.getChanges().isEmpty()) {
            return false;
        }
        log.info("Received {} changes for {}", response.getChanges().size(), jobId());


        ApplyTranslogRequest applyTranslogRequest = ApplyTranslogRequest.builder()
                .shardId(shardRouting.getShardId())
                .indexName(shardRouting.getIndexName())
                .serializedTranslog(response.getChanges())
                .maxSeqNoOfUpdatesOrDeletes(response.getMaxSeqNoOfUpdatesOrDeletes())
                .build();

        ApplyTranslogResponse resp = replicaESClient.replayShardChanges(applyTranslogRequest);

        if (resp.isSuccess()) {

            val checkpoint = ShardCheckpoint.builder()
                    .shardId(shardRouting.getShardId())
                    .indexName(shardRouting.getIndexName())
                    .timestamp(System.currentTimeMillis())
                    .sequence(resp.getSequence())
                    .build();

            log.info("REceived checkpoint: {}", resp.getSequence());
            lastCheckpoint.set(checkpoint);
            translogCheckpointStore.saveCheckpoint(checkpoint);
        } else {
            if (resp.getErrorCode().equals(ApplyTranslogResponse.ErrorCode.INDEX_DOES_NOT_EXISTS)) {
                log.info("Index does not exists.. will reattempt");
            }

            if (resp.getErrorCode().equals(ApplyTranslogResponse.ErrorCode.MAPPING_UPDATE_REQUIRED)) {
                log.info("Mapping update is required.. will wait for mapping to get applied");
            }
        }

        return true;
    }

    private long getStartSequence() {
        return lastCheckpoint.get() == null ? 0 : lastCheckpoint.get().getSequence() + 1;
    }
}
