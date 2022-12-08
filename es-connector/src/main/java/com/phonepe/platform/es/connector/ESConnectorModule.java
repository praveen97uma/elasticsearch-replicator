package com.phonepe.platform.es.connector;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.phonepe.plaftorm.es.replicator.commons.job.Job;
import com.phonepe.plaftorm.es.replicator.commons.job.JobExecutor;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.client.ESClientConfiguration;
import com.phonepe.platform.es.connector.factories.IndexReplicationTaskFactory;
import com.phonepe.platform.es.connector.factories.MetadatPollerTaskFactory;
import com.phonepe.platform.es.connector.factories.ShardReplicationTaskFactory;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.connector.tasks.IndexReplicationTask;
import com.phonepe.platform.es.connector.tasks.MetadataPollingTask;
import com.phonepe.platform.es.connector.tasks.ShardReplicationTask;
import com.phonepe.platform.es.replicator.models.changes.ChangeEvent;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.inject.Named;
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

        install(new FactoryModuleBuilder()
                .implement(Job.class, MetadataPollingTask.class)
                .build(MetadatPollerTaskFactory.class));
    }


    @Provides
    @Singleton
    public JobExecutor<Boolean> jobExecutor() {
        return new JobExecutor<>(Executors.newCachedThreadPool());
    }

    public abstract TranslogCheckpointStore provideTranslogCheckpointStore();


    @Provides
    @Singleton
    @Named("source")
    public ESClient esClient() {
        ESClientConfiguration clientConfiguration = ESClientConfiguration.builder()
                .host("localhost")
                .port(9200)
                .build();

        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(clientConfiguration.getHost(), clientConfiguration.getPort(), "http"));

        return new com.phonepe.platform.es.client.ESRestClientImpl(restClientBuilder);
    }

    @Provides
    @Singleton
    @Named("replica")
    public ESClient provideReplicaESClient() {
        ESClientConfiguration clientConfiguration = ESClientConfiguration.builder()
                .host("localhost")
                .port(9204)
                .build();

        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(clientConfiguration.getHost(), clientConfiguration.getPort(), "http"));

        return new com.phonepe.platform.es.client.ESRestClientImpl(restClientBuilder);
    }

    public abstract EventQueue<ChangeEvent> provideChangeEventSink();
}
