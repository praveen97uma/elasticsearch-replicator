package com.phonepe.platform.es.replicator.queue;

@FunctionalInterface
public interface MessageSerializer<T> {
    byte[] serialize(T value);
}
