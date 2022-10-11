package com.phonepe.plaftorm.es.replicator.commons.impl;

import com.phonepe.plaftorm.es.replicator.commons.AbstractTask;
import com.phonepe.plaftorm.es.replicator.commons.strategy.PollWaitStrategy;
import com.phonepe.plaftorm.es.replicator.commons.strategy.FixedSleepWaitStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class PollingTask extends AbstractTask {
    private final AtomicBoolean cancelled = new AtomicBoolean();
    private ExecutorService executor;

    private final ThreadFactory threadFactory;

    private final PollWaitStrategy pollWaitStrategy;

    public PollingTask(String id) {
        this(id, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName("polling." + id);
            return thread;
        }, new FixedSleepWaitStrategy(10000));
    }

    public PollingTask(String id, final ThreadFactory threadFactory, PollWaitStrategy pollWaitStrategy) {
        super(id);
        this.threadFactory = threadFactory;
        this.pollWaitStrategy = pollWaitStrategy;
    }

    @Override
    public void start() throws Exception {
        if (cancelled.get()) {
            log.error("Task already cancelled. Not starting {}", id());
        }
        this.executor = Executors.newSingleThreadExecutor(threadFactory);
        this.executor.submit(() -> {
            while (true) {
                if (cancelled.get()) {
                    log.info("Task {} was cancelled", id());
                    break;
                }
                try {
                    execute();
                    pollWaitStrategy.waitForNextPoll();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                catch (Exception e) {
                    log.error("Error executing task: ", e);
                }
            }
        });
    }

    @SneakyThrows
    @Override
    public void cancel() {
        this.cancelled.set(true);
        if (this.executor != null) {
            this.executor.shutdown();
            boolean success = this.executor.awaitTermination(5, TimeUnit.SECONDS);
            if (success) {
                log.info("All tasks shutdown successfully");
            } else {
                log.error("Task shutdown timedout or interrupted");
            }
            this.executor = null;
        }
    }
}
