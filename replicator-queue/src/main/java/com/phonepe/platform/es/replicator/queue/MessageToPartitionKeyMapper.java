package com.phonepe.platform.es.replicator.queue;

@FunctionalInterface
public interface MessageToPartitionKeyMapper<T> {
    String toPartitionKey(T message);
}
