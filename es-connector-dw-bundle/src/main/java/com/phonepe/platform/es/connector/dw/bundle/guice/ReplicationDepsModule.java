package com.phonepe.platform.es.connector.dw.bundle.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.phonepe.platform.es.connector.dw.bundle.KafkaEventQueueImpl;
import com.phonepe.platform.es.connector.dw.bundle.ZkTranslogCheckpointStore;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.models.changes.ChangeEvent;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ReplicationDepsModule extends AbstractModule {

    @Provides
    @Singleton
    public CuratorFramework curator() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .namespace("pp-es")
                .connectString("zookeeper001.sb.az6:2181")
                .retryPolicy(new ExponentialBackoffRetry(2000, 100))
                .build();

        curatorFramework.start();
        curatorFramework.blockUntilConnected();

        return curatorFramework;
    }

    @Provides
    @Singleton
    TranslogCheckpointStore provideTranslogCheckpointStore(Injector injector) {
        return injector.getInstance(ZkTranslogCheckpointStore.class);
    }

    @Provides
    @Singleton
    EventQueue<ChangeEvent> provideSink(ObjectMapper mapper) {
        return new KafkaEventQueueImpl(mapper);
    }
}
