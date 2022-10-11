package com.phonepe.platform.es.connector;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.phonepe.plaftorm.es.replicator.commons.job.JobExecutor;
import com.phonepe.plaftorm.es.replicator.commons.lifecycle.ManagedComponentsRunner;
import com.phonepe.platform.es.connector.sink.Sink;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.grpc.events.Events;
import lombok.Builder;

import javax.inject.Singleton;

public class ESREplicationConnector {
    private final ManagedComponentsRunner managedComponentsRunner;
    private final ESReplicationEngine replicationEngine;
    private final Injector injector;


    @Builder
    public ESREplicationConnector(final TranslogCheckpointStore translogCheckpointStore, final Sink<Events.ChangeEvent> changeEventsSink) {
        injector = Guice.createInjector(new ESConnectorModule() {
            @Provides
            @Singleton
            @Override
            public TranslogCheckpointStore provideTranslogCheckpointStore() {
                return translogCheckpointStore;
            }


            @Provides
            @Singleton
            @Override
            public Sink<Events.ChangeEvent> provideChangeEventSink() {
                return changeEventsSink;
            }
        });

        managedComponentsRunner = new ManagedComponentsRunner(injector, "com.phonepe.platform.es.connector");
        replicationEngine = injector.getInstance(ESReplicationEngine.class);

    }


    public void start() {

        managedComponentsRunner.start();
        replicationEngine.start();
    }

    public void stop() {
        managedComponentsRunner.stop();
        replicationEngine.stop();
    }
}
