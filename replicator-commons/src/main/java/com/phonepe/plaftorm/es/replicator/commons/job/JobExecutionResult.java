package com.phonepe.plaftorm.es.replicator.commons.job;

import lombok.Value;

/**
 *
 */
@Value
public class JobExecutionResult <T> {
    String jobId;
    T result;
    Throwable failure;
    boolean cancelled;
}
