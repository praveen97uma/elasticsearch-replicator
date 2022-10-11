package com.phonepe.plaftorm.es.replicator.commons.strategy;

@FunctionalInterface
public interface PollWaitStrategy {
    void waitForNextPoll() throws InterruptedException;
}
