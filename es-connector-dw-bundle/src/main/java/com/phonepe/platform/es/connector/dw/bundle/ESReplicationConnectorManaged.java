package com.phonepe.platform.es.connector.dw.bundle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.phonepe.platform.es.connector.ESReplicationConnector;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.models.changes.ChangeEvent;
import io.dropwizard.lifecycle.Managed;

@Singleton
public class ESReplicationConnectorManaged implements Managed {
    private final ESReplicationConnector esrEplicationConnector;

    @Inject
    public ESReplicationConnectorManaged(TranslogCheckpointStore checkpointStore, EventQueue<ChangeEvent> changeEventEventQueue) {
        esrEplicationConnector = ESReplicationConnector.builder()
                .changeEventsEventQueue(changeEventEventQueue)
                .translogCheckpointStore(checkpointStore)
                .build();
    }

    @Override
    public void start() throws Exception {
        esrEplicationConnector.start();
    }

    @Override
    public void stop() throws Exception {
        esrEplicationConnector.stop();
    }
}
