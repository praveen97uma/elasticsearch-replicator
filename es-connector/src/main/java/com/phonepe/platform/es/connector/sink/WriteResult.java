package com.phonepe.platform.es.connector.sink;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WriteResult {
    boolean success;
    Throwable throwable;
}
