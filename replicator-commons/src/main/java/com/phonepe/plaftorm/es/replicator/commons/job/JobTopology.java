package com.phonepe.plaftorm.es.replicator.commons.job;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class JobTopology<T> implements Job<T> {
    private final AtomicBoolean cancelled = new AtomicBoolean();
    private final List<Job<T>> jobs;
    private final String jobId;

    public JobTopology(final List<Job<T>> jobs) {
        this.jobs = jobs;
        jobId = JobUtils.idFromChildren(jobs);
    }

    @Override
    public String jobId() {
        return jobId;
    }

    public static <T> JobTopology<T> from(final Job<T> job) {
        return JobTopology.<T>builder()
                .addJob(job)
                .build();
    }

    @Override
    public void cancel() {
        cancelled.set(true);
    }

    @Override
    public T execute(JobContext<T> context, JobResponseCombiner<T> responseCombiner) {
        for (Job<T> job : List.copyOf(jobs)) {
            if(!JobUtils.executeSingleJob(context, responseCombiner, job)) {
                log.info("Exiting");
                break;
            }
        }
        return responseCombiner.current();
    }

    @Override
    public boolean isBatch() {
        return true;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder<T> {
        private final List<Job<T>> jobs = new ArrayList<>();
        private ThreadFactory threadFactory;

        public Builder<T> withThreadFactory(final ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public Builder<T> addJob(final Job<T> job) {
            this.jobs.add(job);
            return this;
        }

        public Builder<T> addJob(final List<Job<T>> jobs) {
            this.jobs.addAll(jobs);
            return this;
        }

        @SafeVarargs
        public final Builder<T> addParallel(int parallelism, Job<T>... job) {
            this.jobs.add(new JobLevel<>(parallelism,
                                         Objects.requireNonNullElse(threadFactory, Executors.defaultThreadFactory()),
                                         Arrays.asList(job)));
            return this;
        }

        public Builder<T> addParallel(int parallelism, List<Job<T>> jobs) {
            this.jobs.add(new JobLevel<>(parallelism,
                                         Objects.requireNonNullElse(threadFactory, Executors.defaultThreadFactory()),
                                         jobs));
            return this;
        }

        public JobTopology<T> build() {
            return new JobTopology<>(List.copyOf(jobs));
        }
    }

    public static<T> Builder<T> builder() {
        return new Builder<>();
    }
}
