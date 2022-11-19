package com.phonepe.platform.es.replicator.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@AllArgsConstructor
public class KafkaProducerConfig {
    String bootstrapServers;

    String topicName;

    Map<String, String> properties;
}
