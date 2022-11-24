package com.phonepe.platform.es.connector.dw.bundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.plaftorm.es.replicator.commons.queue.EventQueue;
import com.phonepe.plaftorm.es.replicator.commons.queue.QueueType;
import com.phonepe.plaftorm.es.replicator.commons.queue.WriteResult;
import com.phonepe.platform.es.replicator.models.changes.ChangeEvent;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.impl.KafkaProducerRecordImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class KafkaEventQueueImpl implements EventQueue<ChangeEvent> {
    private final KafkaProducer<String, ChangeEvent> producer;
    private final ObjectMapper mapper;

    public KafkaEventQueueImpl(final ObjectMapper mapper) {
        this.mapper = mapper;

        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setEventLoopPoolSize(4);
        vertxOptions.setPreferNativeTransport(true);

        Vertx vertx = Vertx.vertx();

        Map<String, String> config = new HashMap<>();

        config.put("bootstrap.servers", "stg-stkbrk-kafka-001.sb.az6:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = KafkaProducer.createShared(vertx, "message-sender", config,
                new StringSerializer(), (s, changeEvent) -> {
                    try {
                        return mapper.writeValueAsBytes(changeEvent);
                    } catch (Exception e) {
                        log.info("Error serializing ", e);
                        return null;
                    }
                });

    }

    @Override
    public QueueType type() {
        return QueueType.KAFKA;
    }

    @Override
    public WriteResult write(final ChangeEvent change) throws Exception {
        return writeBatch(List.of(change));
    }

    @Override
    public WriteResult writeBatch(final List<ChangeEvent> changes) throws Exception {
        changes.forEach(event -> {
            KafkaProducerRecord<String, ChangeEvent> srecord = new KafkaProducerRecordImpl<>(
                    "es-changes-v1",
                    "event",
                    event);

            producer.send(srecord);
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
