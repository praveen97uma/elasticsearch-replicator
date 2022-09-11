package com.phonepe.plaftorm.es.replicator.changes.plugin.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.single.shard.SingleShardRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.shard.ShardId;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class GetChangesRequest extends SingleShardRequest<GetChangesRequest> {
    private ShardId shardId;

    private long fromSeqNo;

    private long toSeqNo;

    public GetChangesRequest(ShardId shardId, long fromSeqNo, long toSeqNo) {
        super(shardId.getIndexName());
        this.shardId = shardId;
        this.fromSeqNo = fromSeqNo;
        this.toSeqNo = toSeqNo;
    }

    public GetChangesRequest(StreamInput in) throws IOException {
        super(in);
        this.shardId = new ShardId(in);
        fromSeqNo = in.readLong();
        toSeqNo = in.readLong();
    }

    @Override
    public ActionRequestValidationException validate() {
        return validateNonNullIndex();
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        this.shardId = new ShardId(in);
        fromSeqNo = in.readLong();
        toSeqNo = in.readLong();

    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        shardId.writeTo(out);
        out.writeLong(fromSeqNo);
        out.writeLong(toSeqNo);
    }
}
