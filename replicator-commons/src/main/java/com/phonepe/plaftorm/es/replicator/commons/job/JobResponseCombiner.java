package com.phonepe.plaftorm.es.replicator.commons.job;


public interface JobResponseCombiner<T>{
    void combine(Job<T> job, final T newResponse);
    boolean handleError(final Throwable throwable);
    void handleCancel();
    T current();
    JobExecutionResult<T> buildResult(String jobId);
}
