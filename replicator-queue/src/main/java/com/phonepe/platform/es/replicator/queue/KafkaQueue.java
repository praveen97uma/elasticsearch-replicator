package com.phonepe.platform.es.replicator.queue;

import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.plaftorm.es.replicator.commons.queue.QueueType;
import com.phonepe.plaftorm.es.replicator.commons.queue.WriteResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;
import io.vertx.kafka.client.producer.impl.KafkaProducerRecordImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class KafkaQueue<T> implements EventQueue<T> {
    private final KafkaProducer<String, T> producer;

    private final MessageToPartitionKeyMapper<T> messageToPartitionKeyMapper;

    private final KafkaProducerConfig producerConfig;

    public KafkaQueue(final Vertx vertx,
                      final KafkaProducerConfig producerConfig,
                      @NonNull final MessageSerializer<T> messageSerializer,
                      @Nullable final MessageToPartitionKeyMapper<T> messageToPartitionKeyMapper) {
        this.producerConfig = producerConfig;
        this.producer = KafkaProducer.createShared(vertx, "message-sender", producerConfig.getProperties(),
                new StringSerializer(),
                (topic, message) -> messageSerializer.serialize(message));
        this.messageToPartitionKeyMapper = messageToPartitionKeyMapper;
    }

    @Override
    public QueueType type() {
        return QueueType.KAFKA;
    }

    @Override
    public WriteResult write(final T change) throws Exception {
        return writeBatch(List.of(change));
    }

    @Override
    public WriteResult writeBatch(final List<T> changes) throws Exception {
        changes.forEach(event -> {
            KafkaProducerRecord<String, T> record = messageToPartitionKeyMapper == null
                    ? new KafkaProducerRecordImpl<>(producerConfig.getTopicName(), event)
                    : new KafkaProducerRecordImpl<>(
                            producerConfig.getTopicName(),
                            messageToPartitionKeyMapper.toPartitionKey(event),
                            event);

            producer.send(record);
        });

        CompletableFuture<WriteResult> future = new CompletableFuture<>();

        producer.flush(result -> {
            if (result.succeeded()) {
                future.complete(WriteResult.builder()
                        .success(true)
                        .build());
            } else {
                future.complete(WriteResult.builder()
                        .success(false)
                        .throwable(result.cause())
                        .build());
            }
        });

        return future.get();
    }

    @Override
    public void close() {
        producer.close();
    }
}
