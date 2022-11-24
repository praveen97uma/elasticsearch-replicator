package com.phonepe.platform.es.replicator.models;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Value
@Builder
@Jacksonized
public class ChangeData {
    SerializedTranslog translog;
    long connectorSentTimestamp;
}
