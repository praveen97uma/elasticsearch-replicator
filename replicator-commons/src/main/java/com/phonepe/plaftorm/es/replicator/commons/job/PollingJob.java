package com.phonepe.plaftorm.es.replicator.commons.job;

import com.phonepe.plaftorm.es.replicator.commons.strategy.FixedSleepWaitStrategy;
import com.phonepe.plaftorm.es.replicator.commons.strategy.PollWaitStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public abstract class PollingJob<T> implements Job<T> {
    private final AtomicBoolean cancelled = new AtomicBoolean();

    private final ExecutorService executor;

    private final PollWaitStrategy pollWaitStrategy;

    public PollingJob() {
        this(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName("polling.job");
            return thread;
        }, new FixedSleepWaitStrategy(10000));
    }

    public PollingJob(final ThreadFactory threadFactory, PollWaitStrategy pollWaitStrategy) {
        this.pollWaitStrategy = pollWaitStrategy;
        this.executor = Executors.newSingleThreadExecutor(threadFactory);
    }


    @SneakyThrows
    @Override
    public void cancel() {
        this.cancelled.set(true);

//        if (!executor.isShutdown()) {
//            this.executor.shutdownNow();
//            boolean success = this.executor.awaitTermination(5, TimeUnit.SECONDS);
//            if (success) {
//                log.info("All tasks shutdown successfully");
//            } else {
//                log.error("Task shutdown timedout or interrupted");
//            }
//        }
    }

    @Override
    public T execute(final JobContext<T> context, final JobResponseCombiner<T> responseCombiner) {
//        this.executor.submit(() -> {
            this.init();
            while (true) {

                if (cancelled.get() || context.isCancelled() || context.isStopped()) {
                    log.info("Task {} was cancelled/stopped. Stopping polling", jobId());
                    break;
                }

                try {
                    responseCombiner.combine(this, poll(context, responseCombiner)); ;
                    pollWaitStrategy.waitForNextPoll();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                catch (Exception e) {
                    log.error("Error executing task: ", e);
//                    responseCombiner.handleError(e);
                }
            }
//        });

        return responseCombiner.current();
    }

    public abstract void init();

    public abstract T poll(final JobContext<T> context, final JobResponseCombiner<T> responseCombiner);
}
