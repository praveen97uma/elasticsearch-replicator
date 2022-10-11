package com.phonepe.plaftorm.es.replicator.commons.strategy;

public class FixedSleepWaitStrategy implements PollWaitStrategy {
    private final int sleepTimeMs;

    public FixedSleepWaitStrategy(int sleepTimeMs) {
        this.sleepTimeMs = sleepTimeMs;
    }
    @Override
    public void waitForNextPoll() throws InterruptedException {
        Thread.sleep(sleepTimeMs);
    }
}
