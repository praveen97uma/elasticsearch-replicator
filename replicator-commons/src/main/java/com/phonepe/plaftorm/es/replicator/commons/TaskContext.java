package com.phonepe.plaftorm.es.replicator.commons;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TaskContext<T> {
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final AtomicBoolean cancelled = new AtomicBoolean();

    @Getter
    private Consumer<TaskExecutionResult<T>> handler;

    @Setter
    @Getter
    private Future<TaskExecutionResult<T>> future;

    public TaskContext(Consumer<TaskExecutionResult<T>> handler) {
        this.handler = handler;
    }

    public void markStopped() {
        stopped.set(true);
    }

    public void markCancelled() {
        cancelled.set(true);
    }

    public boolean isStopped() {
        return stopped.get();
    }

    public boolean isCancelled() {
        return cancelled.get();
    }
}
