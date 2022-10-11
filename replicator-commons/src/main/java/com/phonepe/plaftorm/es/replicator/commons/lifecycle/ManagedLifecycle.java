package com.phonepe.plaftorm.es.replicator.commons.lifecycle;

public interface ManagedLifecycle {
    void start() throws Exception;
    void stop() throws Exception;
}
