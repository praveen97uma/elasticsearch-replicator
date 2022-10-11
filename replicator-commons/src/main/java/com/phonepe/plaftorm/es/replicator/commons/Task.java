package com.phonepe.plaftorm.es.replicator.commons;

import com.phonepe.plaftorm.es.replicator.commons.interfaces.Cancellable;
import com.phonepe.plaftorm.es.replicator.commons.interfaces.Lifecycle;

public interface Task extends Lifecycle, Cancellable {
    String id();

    void execute();
}
