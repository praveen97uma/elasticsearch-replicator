package com.phonepe.plaftorm.es.replicator.commons;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractTask implements Task {
    private final String id;

    private final AtomicBoolean stopped = new AtomicBoolean();
    private final AtomicBoolean cancelled = new AtomicBoolean();

    public AbstractTask(String id) {
        this.id = id;
    }
    @Override
    public String id() {
        return this.id;
    }

    @Override
    public void cancel() {
        this.cancelled.set(true);
    }

    @Override
    public void start() throws Exception {
        if (this.cancelled.get()) {
            log.error("Task already cancelled. Can not start {}", id());
            return;
        }
        stopped.set(false);
        execute();
    }

    @Override
    public void stop() throws Exception {
        this.stopped.set(true);
    }
}
