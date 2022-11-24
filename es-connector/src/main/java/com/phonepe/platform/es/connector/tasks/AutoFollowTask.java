package com.phonepe.platform.es.connector.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.phonepe.plaftorm.es.replicator.commons.impl.PollingTask;
import com.phonepe.plaftorm.es.replicator.commons.job.BooleanResponseCombiner;
import com.phonepe.plaftorm.es.replicator.commons.job.JobExecutor;
import com.phonepe.plaftorm.es.replicator.commons.job.JobTopology;
import com.phonepe.plaftorm.es.replicator.commons.lifecycle.ManagedLifecycle;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.connector.factories.IndexReplicationTaskFactory;
import com.phonepe.platform.es.connector.models.IndexReplicateRequest;
import com.phonepe.platform.es.replicator.models.GetIndexAndShardsMetadataRequest;
import com.phonepe.platform.es.replicator.models.GetIndexAndShardsMetadataResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Singleton
public class AutoFollowTask extends PollingTask implements ManagedLifecycle {
    private final ESClient esClient;

    private final JobExecutor<Boolean> jobExecutor;

    private final IndexReplicationTaskFactory replicationTaskFactory;

    @Inject
    public AutoFollowTask(String id, ESClient esClient, final JobExecutor<Boolean> jobExecutor, final IndexReplicationTaskFactory replicationTaskFactory) {
        super(id);
        this.esClient = esClient;
        this.jobExecutor = jobExecutor;
        this.replicationTaskFactory = replicationTaskFactory;
    }

    @Override
    public void execute() {
        GetIndexAndShardsMetadataRequest request = GetIndexAndShardsMetadataRequest.builder()
                .build();

        try {
            GetIndexAndShardsMetadataResponse response = esClient.getIndexAndShardsMetadata(request);
//        log.info("Index metadatas {}", response);

            response.getIndexMetadatas().stream()
                    .map(indexMetadata -> IndexReplicateRequest.builder()
                            .currentNodeId(response.getCurrentNodeId())
                            .indexMetadata(indexMetadata)
                            .build())
                    .forEach(this::handleIndexReplication);
        } catch (Exception e) {
            log.error("Error ", e);
        }
    }

    private void handleIndexReplication(final IndexReplicateRequest replicateRequest) {
        // we fire this task at every poll. The task takes care of launching/stopping/aborting
        // tasks for every shard on this node
        IndexReplicationTask task = replicationTaskFactory.create(replicateRequest);

        jobExecutor.schedule(
                JobTopology.from(task),
                new BooleanResponseCombiner(),
                r -> {});
    }
}
