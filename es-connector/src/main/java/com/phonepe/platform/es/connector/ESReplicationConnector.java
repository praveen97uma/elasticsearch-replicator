package com.phonepe.platform.es.connector;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.phonepe.plaftorm.es.replicator.commons.lifecycle.ManagedComponentsRunner;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.models.changes.ChangeEvent;
import lombok.Builder;

import javax.inject.Singleton;

public class ESReplicationConnector {
    private final ManagedComponentsRunner managedComponentsRunner;
    private final ESReplicationEngine replicationEngine;
    private final Injector injector;


    @Builder
    public ESReplicationConnector(final TranslogCheckpointStore translogCheckpointStore, final EventQueue<ChangeEvent> changeEventsEventQueue) {
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
            public EventQueue<ChangeEvent> provideChangeEventSink() {
                return changeEventsEventQueue;
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
