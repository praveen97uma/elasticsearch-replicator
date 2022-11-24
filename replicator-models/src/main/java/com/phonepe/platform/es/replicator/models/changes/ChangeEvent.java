package com.phonepe.platform.es.replicator.models.changes;

import com.phonepe.platform.es.replicator.models.SerializedTranslog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeEvent {
    SerializedTranslog translog;
    long connectorSentTimestamp = 2;
}
