package com.phonepe.plaftorm.es.replicator.commons;

import lombok.Value;

@Value
public class TaskExecutionResult<T> {
    String jobId;
    T result;
    Throwable failure;
    boolean cancelled;
}
