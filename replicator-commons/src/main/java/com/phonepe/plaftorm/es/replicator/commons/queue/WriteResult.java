package com.phonepe.plaftorm.es.replicator.commons.queue;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WriteResult {
    boolean success;
    Throwable throwable;
}
