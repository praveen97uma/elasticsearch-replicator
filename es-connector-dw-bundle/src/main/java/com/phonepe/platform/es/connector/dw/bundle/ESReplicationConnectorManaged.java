package com.phonepe.platform.es.connector.dw.bundle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.phonepe.platform.es.connector.ESREplicationConnector;
import com.phonepe.platform.es.connector.sink.Sink;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.grpc.events.Events;
import io.dropwizard.lifecycle.Managed;

@Singleton
public class ESReplicationConnectorManaged implements Managed {
    private final ESREplicationConnector esrEplicationConnector;

    @Inject
    public ESReplicationConnectorManaged(TranslogCheckpointStore checkpointStore, Sink<Events.ChangeEvent> changeEventSink) {
        esrEplicationConnector = ESREplicationConnector.builder()
                .changeEventsSink(changeEventSink)
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
