package com.phonepe.platform.es.connector.tasks;

import com.google.inject.Inject;
//import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.Assisted;
import com.phonepe.plaftorm.es.replicator.commons.job.*;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.connector.factories.ShardReplicationTaskFactory;
import com.phonepe.platform.es.connector.models.IndexReplicateRequest;
import com.phonepe.platform.es.connector.models.ShardReplicateRequest;
import com.phonepe.platform.es.replicator.models.EsShardRouting;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class IndexReplicationTask implements Job<Boolean> {
    private final IndexReplicateRequest indexReplicateRequest;
    private final JobExecutor<Boolean> jobExecutor;

    private final ShardReplicationTaskFactory shardReplicationTaskFactory;

    private final ESClient esClient;

    @Inject
    @Builder
    public IndexReplicationTask(@Assisted final IndexReplicateRequest indexReplicateRequest,
                                final JobExecutor<Boolean> jobExecutor,
                                final ShardReplicationTaskFactory shardReplicationTaskFactory,
                                final ESClient esClient) {

        this.indexReplicateRequest = indexReplicateRequest;
        this.jobExecutor = jobExecutor;
        this.shardReplicationTaskFactory = shardReplicationTaskFactory;
        this.esClient = esClient;
    }

    @Override
    public String jobId() {
        return indexReplicateRequest.getIndexMetadata().getIndexName();
    }

    @Override
    public void cancel() {

    }

    @Override
    public Boolean execute(final JobContext<Boolean> context, final JobResponseCombiner<Boolean> responseCombiner) {
        /*

        case1: no task present for a shard in running state -> start new tasks starting from any translog checkpoints if present
        case2: if task present, then
            2.1: if shard is running and task is also in running state -> NOOP
            2.1: if shard goes to not running and task state is running -> cancel and de-register the shard polling task

         */


        launchJobsForNewShards();


        return true;
    }

    private void launchJobsForNewShards() {
        List<EsShardRouting> newShards = indexReplicateRequest.getIndexMetadata().getShardRoutings().stream()
                .filter(routing -> routing.getState().equals("STARTED"))
                .filter(routing -> routing.getNodeId().equals(indexReplicateRequest.getCurrentNodeId()))
                .filter(routing -> !jobExecutor.isJobRunning(ShardReplicateRequest.fromShardRouting(routing)))
                .collect(Collectors.toList());

        if (newShards.isEmpty()) {
            return;
        }

        log.info("{} shards are not running", newShards.size());

        List<Job<Boolean>> jobs = newShards.stream()
                .map(esShardRouting -> shardReplicationTaskFactory.create(ShardReplicateRequest.builder()
                                .indexMetadata(indexReplicateRequest.getIndexMetadata())
                                .currentNodeId(indexReplicateRequest.getCurrentNodeId())
                                .shardRouting(esShardRouting)
                                .build()))
                .collect(Collectors.toList());

        log.info("Launching jobs for {}", newShards);

        jobExecutor.scheduleAll(jobs, new BooleanResponseCombiner(), x -> {
//            log.info("IndexReplicationTask: Task Exec result: {}", x);
        });
    }
}
