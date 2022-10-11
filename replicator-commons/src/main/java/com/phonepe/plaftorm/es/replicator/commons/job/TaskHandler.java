package com.phonepe.plaftorm.es.replicator.commons.job;

public interface TaskHandler<T> {
    void handle(T request);
}
