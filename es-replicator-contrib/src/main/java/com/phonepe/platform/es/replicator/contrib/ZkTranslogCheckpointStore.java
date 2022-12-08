package com.phonepe.platform.es.replicator.contrib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.phonepe.platform.es.connector.store.ShardCheckpoint;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.Optional;

@Slf4j
public class ZkTranslogCheckpointStore implements TranslogCheckpointStore {
    private final CuratorFramework curatorFramework;
    private final ObjectMapper mapper;

    @Inject
    public ZkTranslogCheckpointStore(final CuratorFramework curatorFramework, final ObjectMapper mapper) {
        this.curatorFramework = curatorFramework;
        this.mapper = mapper;
    }

    @Override
    public void saveCheckpoint(final ShardCheckpoint checkpoint) {
        try {
            String path = getPath(checkpoint.getIndexName(), checkpoint.getShardId());
            if (curatorFramework.checkExists().forPath(path) == null) {
                log.info("ZNode doesn't exists for path: {}, creating", path);
                curatorFramework.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(path, mapper.writeValueAsBytes(checkpoint));
            } else {
                log.info("ZNode  exists for path: {}, updating checkpoint to {}", path, checkpoint.getSequence());
                curatorFramework.setData()
                        .forPath(path, mapper.writeValueAsBytes(checkpoint));
            }
        } catch (Exception e) {
            log.error("Error ", e);
            throw new RuntimeException(e);
        }
    }

    private static String getPath(final String indexName, int shardId) {
        return String.format("/checkpoint/%s/%d", indexName, shardId);
    }

    @Override
    public Optional<ShardCheckpoint> getCheckpoint(final String indexName, final int shardId) {
        try {
            String path = getPath(indexName, shardId);

            if (curatorFramework.checkExists().forPath(path) == null) {
                log.info("No checkpoint found for {}", path);
                return Optional.empty();
            }

            byte[] data = curatorFramework.getData()
                    .forPath(path);

            return Optional.of(mapper.readValue(data, ShardCheckpoint.class));
        } catch (Exception e) {
            log.error("Error ", e);
            throw new RuntimeException(e);
        }
    }
}
