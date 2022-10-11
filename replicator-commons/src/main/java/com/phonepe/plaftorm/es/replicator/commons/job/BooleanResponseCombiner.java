package com.phonepe.plaftorm.es.replicator.commons.job;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This will mimic an OR combiner
 */
public class BooleanResponseCombiner extends AbstractJobResponseCombiner<Boolean> {
    private final AtomicBoolean current = new AtomicBoolean();

    @Override
    public void combine(Job<Boolean> job, Boolean newResponse) {
        current.compareAndSet(false, newResponse);
    }

    @Override
    public Boolean current() {
        return current.get();
    }

    @Override
    public JobExecutionResult<Boolean> buildResult(String jobId) {
        return new JobExecutionResult<>(jobId, current(), super.failure.get(), super.cancelled.get());
    }
}
