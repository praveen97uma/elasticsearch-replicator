package com.phonepe.plaftorm.es.replicator.commons.scheduler;

import com.phonepe.plaftorm.es.replicator.commons.Task;

public abstract class AbstractPollingScheduler {
    public void startPolling(Task runnable) {
        this.schedule(runnable);
    }
    protected abstract void schedule(Task runnable);

    public abstract void stop();
}
