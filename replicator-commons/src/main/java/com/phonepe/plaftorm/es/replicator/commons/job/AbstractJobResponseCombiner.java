package com.phonepe.plaftorm.es.replicator.commons.job;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public abstract class AbstractJobResponseCombiner<T> implements JobResponseCombiner<T> {
    protected final AtomicReference<Throwable> failure = new AtomicReference<>();
    protected final AtomicBoolean cancelled = new AtomicBoolean();

    @Override
    public boolean handleError(Throwable throwable) {
        failure.set(throwable);
        return false;
    }

    @Override
    public void handleCancel() {
        cancelled.set(true);
    }

}
