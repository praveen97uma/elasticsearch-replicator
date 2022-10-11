package com.phonepe.platform.es.connector.sink;

import java.util.List;

public interface Sink<T> {
    SinkType type();
    WriteResult write(T changes) throws Exception;

    WriteResult writeBatch(List<T> changes) throws Exception;
    void close();
}
