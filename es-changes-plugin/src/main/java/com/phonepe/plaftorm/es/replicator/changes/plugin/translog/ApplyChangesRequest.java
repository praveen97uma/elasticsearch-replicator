package com.phonepe.plaftorm.es.replicator.changes.plugin.translog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.single.shard.SingleShardRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApplyChangesRequest extends SingleShardRequest<ApplyChangesRequest> {
    private ShardId shardId;

    private Collection<Translog.Operation> changes;

    public ApplyChangesRequest(ShardId shardId, List<Translog.Operation> operations) {
        super(shardId.getIndexName());
        this.shardId = shardId;
        this.changes = operations;
    }

    public ApplyChangesRequest(StreamInput in) throws IOException {
        super(in);
        this.shardId = new ShardId(in);
        this.changes = in.readList(Translog.Operation::readOperation);
    }

    @Override
    public ActionRequestValidationException validate() {
        return validateNonNullIndex();
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        this.shardId = new ShardId(in);
        this.changes = in.readList(Translog.Operation::readOperation);

    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        shardId.writeTo(out);
        out.writeCollection(changes, Translog.Operation::writeOperation);
    }
}
