package com.phonepe.platform.es.connector;

import ch.qos.logback.classic.Level;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ChangePoller {
//    public static ChangesEngineGrpc.ChangesEngineBlockingStub stub;
//    public static ChangesEngineGrpc.ChangesEngineBlockingStub replicaStub;
//
//    public ChangePoller() {
//        ch.qos.logback.classic.Logger nettyLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("io.grpc");
//        nettyLogger.setLevel(Level.OFF);
//
//        String target = "localhost:9400";
//        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
//                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
//                // needing certificates.
//                .usePlaintext()
//                .build();
//
//        stub = ChangesEngineGrpc.newBlockingStub(channel);
//
//        String replicaTarget = "localhost:9401";
//        ManagedChannel replicaChannel = ManagedChannelBuilder.forTarget(replicaTarget)
//                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
//                // needing certificates.
//                .usePlaintext()
//                .build();
//
//        replicaStub = ChangesEngineGrpc.newBlockingStub(replicaChannel);
//    }
//
//    @SneakyThrows
//    public static void main(String... args) {
//        ChangePoller poller = new ChangePoller();
//
//        stub.getIndexMetadata(GetIndexMetadataRequest.newBuilder()
//                        .setIndexName("bob")
//                .build());
//
//        Thread.sleep(100000);
//
//        GetIndexAndShardsMetadataResponse indexMetadatas = stub.getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest.newBuilder().build());
//        log.info("Index metadatas {}", indexMetadatas);
//
//        String indexName = "bob";
//        String indexUUID = "j5QJGleVQmGp_U17i66jCA";
//        int shardId = 1;
//
//        ESShardId esShardId = ESShardId.newBuilder()
//                .setIndexName(indexName)
//                .setIndexUUID(indexUUID)
//                .setShardId(shardId)
//                .build();
//
//        AtomicLong lastSeq = new AtomicLong();
//        AtomicLong syncedSeq = new AtomicLong();
//
//        while (true) {
//            long start = syncedSeq.get() == 0 ? 0: syncedSeq.get() + 1;
//            GetChangesRequest request = GetChangesRequest.newBuilder()
//                    .setIndexName("bob")
//                    .setIndexUUID(indexUUID)
//                    .setShardId(shardId)
//                    .setFromSeqNo(start)
//                    .setToSeqNo(start + 200)
//                    .build();
//
//            GetChangesResponse r = ChangePoller.stub.getShardChanges(request);
//            log.info("Retrieved no of ops: {} from sequence: {}", r.getChangesList().size(), request.getFromSeqNo());
//            r.getChangesList().forEach(translog -> {
//                ApplyTranslogResponse response = replicaStub.applyTranslog(ApplyTranslogRequest.newBuilder()
//                        .setShardId(esShardId)
//                        .setTranslog(translog)
//                        .build());
//                syncedSeq.set(Math.max(response.getSequence(), syncedSeq.get()));
//                log.info("Main apply translog result : {}", response.toString());
//
//                if (!response.getSuccess()) {
//                    if (response.getErrorCode().equals(ApplyTranslogResponse.ErrorCode.INDEX_DOES_NOT_EXISTS)) {
//                        ESIndexMetadata imetaData = stub.getIndexMetadata(GetIndexMetadataRequest.newBuilder()
//                                .setIndexName(indexName)
//                                .build()).getIndexMetadata();
//                        ESCreateIndexResponse createIndexResponse = replicaStub.createIndex(ESCreateIndexRequest.newBuilder()
//                                .setIndexMetadata(imetaData)
//                                .build());
//                        log.error("Index creation response {}", createIndexResponse);
//                    }
//
//                    if (response.getError().equals("MAPPING_UPDATE_REQUIRED")) {
//                        log.info("Creating mapping");
//                        GetMappingResponse mappingResponse = stub.getMapping(GetMappingRequest.newBuilder()
//                                .setIndexName(indexName)
//                                .build());
//
//                        log.info("Mapping fetched: {}", mappingResponse.getMappingSource());
//
//                        ApplyMappingResponse applyMappingResponse = replicaStub.syncMapping(ApplyMappingRequest.newBuilder()
//                                        .setIndexName(indexName)
//                                        .setMappingSource(mappingResponse.getMappingSource())
//                                .build());
//                        log.info("Mapping created: {}", applyMappingResponse.getSuccess());
//                    }
//
//                    ApplyTranslogResponse appResponse = replicaStub.applyTranslog(ApplyTranslogRequest.newBuilder()
//                            .setShardId(esShardId)
//                            .setTranslog(translog)
//                            .build());
//                    log.info("Translog application response {}", appResponse.toString());
//
//                }
//                log.info("Translog applicatio response {}", response);
//            });
//            Thread.sleep(5000);
//        }
//    }
}
