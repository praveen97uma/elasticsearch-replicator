package com.phonepe.platform.es.connector;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.phonepe.plaftorm.es.replicator.commons.job.Job;
import com.phonepe.plaftorm.es.replicator.commons.job.JobExecutor;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.client.ESClientConfiguration;
import com.phonepe.platform.es.client.ESGrpcClient;
import com.phonepe.platform.es.connector.factories.IndexReplicationTaskFactory;
import com.phonepe.platform.es.connector.factories.ShardReplicationTaskFactory;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.connector.tasks.IndexReplicationTask;
import com.phonepe.platform.es.connector.tasks.ShardReplicationTask;
import com.phonepe.platform.es.replicator.grpc.events.Events;

import javax.inject.Singleton;
import java.util.concurrent.Executors;

public abstract class ESConnectorModule extends AbstractModule {

    @Override
    public void configure() {
        install(new FactoryModuleBuilder()
                .implement(Job.class, IndexReplicationTask.class)
                .build(IndexReplicationTaskFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Job.class, ShardReplicationTask.class)
                .build(ShardReplicationTaskFactory.class));
    }


    @Provides
    @Singleton
    public JobExecutor<Boolean> jobExecutor() {
        return new JobExecutor<>(Executors.newCachedThreadPool());
    }

    public abstract TranslogCheckpointStore provideTranslogCheckpointStore();


    @Provides
    @Singleton
    public ESClient esClient() {
        ESClientConfiguration clientConfiguration = ESClientConfiguration.builder()
                .host("localhost")
                .port(9400)
                .build();

        return new ESGrpcClient(clientConfiguration);
    }

    public abstract EventQueue<Events.ChangeEvent> provideChangeEventSink();
}
