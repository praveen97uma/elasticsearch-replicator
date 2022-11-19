package com.phonepe.plaftorm.es.replicator.commons.queue;

import java.util.List;

public interface EventQueue<T> {
    QueueType type();
    WriteResult write(T changes) throws Exception;

    WriteResult writeBatch(List<T> changes) throws Exception;
    void close();
}
