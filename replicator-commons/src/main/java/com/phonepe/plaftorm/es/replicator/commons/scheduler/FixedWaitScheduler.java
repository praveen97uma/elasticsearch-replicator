package com.phonepe.plaftorm.es.replicator.commons.scheduler;

import com.phonepe.plaftorm.es.replicator.commons.Task;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FixedWaitScheduler extends AbstractPollingScheduler {
    private ScheduledExecutorService executor;
    private int initialDelayMillis = 30000;
    private int delayMillis = 60000;

    public FixedWaitScheduler() {
    }

    @Override
    protected void schedule(Task task) {
        this.executor = Executors.newScheduledThreadPool(1, runnable -> {
            Thread thread = new Thread(runnable, "polling." +  task.id());
            thread.setDaemon(true);
            return thread;
        });
        this.executor.scheduleWithFixedDelay(task::execute, this.initialDelayMillis, this.delayMillis, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    @Override
    public void stop() {
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
