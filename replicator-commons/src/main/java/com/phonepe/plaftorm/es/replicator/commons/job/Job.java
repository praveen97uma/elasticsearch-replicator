package com.phonepe.plaftorm.es.replicator.commons.job;

/**
 *
 */
public interface Job<T> {

    String jobId();

    void cancel();

    T execute(
            JobContext<T> context,
            final JobResponseCombiner<T> responseCombiner);

    default boolean isBatch() {
        return false;
    }
}
