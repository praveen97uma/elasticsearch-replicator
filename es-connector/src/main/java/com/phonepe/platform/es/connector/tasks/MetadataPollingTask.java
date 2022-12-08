package com.phonepe.platform.es.connector.tasks;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.phonepe.plaftorm.es.replicator.commons.job.JobContext;
import com.phonepe.plaftorm.es.replicator.commons.job.JobResponseCombiner;
import com.phonepe.plaftorm.es.replicator.commons.job.PollingJob;
import com.phonepe.platform.es.client.ESClient;
import com.phonepe.platform.es.connector.store.ShardCheckpoint;
import com.phonepe.platform.es.replicator.models.*;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;

import javax.inject.Named;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
public class MetadataPollingTask extends PollingJob<Boolean> {

    private final ESClient sourceEsClient;

    private final ESClient replicaEsClient;

    private final String indexName;

    @Inject
    @Builder
    public MetadataPollingTask(@Assisted final String indexName,
                               final @Named("source") ESClient sourceEsClient,
                               final @Named("replica") ESClient replicaEsClient
                               ) {
        this.sourceEsClient = sourceEsClient;
        this.replicaEsClient = replicaEsClient;
        this.indexName = indexName;
    }

    @Override
    public String jobId() {
        return indexName;
    }

    @Override
    public void init() {

    }

    @Override
    @SneakyThrows
    public Boolean poll(final JobContext<Boolean> context, final JobResponseCombiner<Boolean> responseCombiner) {
//        GetIndexAndShardsMetadataResponse metadataResponse = sourceEsClient.getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest.builder()
//                .build());
//
//        GetIndexAndShardsMetadataResponse replicaMetadata = replicaEsClient.getIndexAndShardsMetadata(GetIndexAndShardsMetadataRequest.builder().build());
//
//
//        Map<String, EsIndexMetadata> replicaMetadtaMap = replicaMetadata.getIndexMetadatas().stream()
//                .collect(Collectors.toMap(EsIndexMetadata::getIndexName, Function.identity()));
//
//        metadataResponse.getIndexMetadatas().forEach(esIndexMetadata -> {
//            if (!replicaMetadtaMap.containsKey(esIndexMetadata.getIndexName())) {
//                try {
//                    CreateIndexResponse response = replicaEsClient.createIndex(CreateIndexRequest.builder()
//                            .esIndexMetadata(esIndexMetadata)
//                            .build());
//
//                    log.info("Create index response: {}", response);
//                } catch (IOException e) {
//                    log.error("Error ", e);
//                }
//            }
//        });
//
//        GetMappingsResponse mappingsResponse = sourceEsClient.getMappings(indexName);
//        MappingMetaData mappingMetaData = mappingsResponse.mappings().get(indexName);
//
//        if (mappingMetaData == null) {
//            return true;
//        }
//
//        log.info("Received mapping {}", mappingMetaData.getSourceAsMap());
//
//        log.info("Replica mapping {}", replicaEsClient.getMappings(indexName).mappings().get(indexName).sourceAsMap());
//
//        AcknowledgedResponse resp = replicaEsClient.createMappings(indexName, mappingMetaData.source().string());
//
//        log.info("Updated mapping for index {}, {}", resp.isAcknowledged(), indexName);
        return true;
    }
}
