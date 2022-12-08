package com.phonepe.plaftorm.es.replicator.changes.plugin.exceptions;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.index.shard.ShardId;

import java.io.IOException;

public class MappingUpdateRequiredException extends ElasticsearchException {

    public MappingUpdateRequiredException(ShardId shardId, Object... params) {
        this(shardId, "mapping update required", null, params);
    }

    public MappingUpdateRequiredException(ShardId shardId, String msg, Throwable cause, Object... params) {
        super(msg, cause, params);
        setShard(shardId);
    }

    public MappingUpdateRequiredException(StreamInput in) throws IOException {
        super(in);
    }
}
