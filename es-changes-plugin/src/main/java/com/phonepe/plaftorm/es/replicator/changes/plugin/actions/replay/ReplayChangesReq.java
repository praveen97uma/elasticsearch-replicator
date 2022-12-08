package com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay;

import lombok.Getter;
import org.elasticsearch.action.support.replication.ReplicatedWriteRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;

import java.io.IOException;
import java.util.List;

@Getter
public class ReplayChangesReq extends ReplicatedWriteRequest<ReplayChangesReq> {
    private final String leaderIndex;
    private final List<Translog.Operation> changes;
    private final long maxSeqNoOfUpdatesOrDeletes;



    public ReplayChangesReq(final StreamInput in) throws IOException {
        super(in);
        this.leaderIndex = in.readString();
        this.changes = in.readList(Translog.Operation::readOperation);
        this.maxSeqNoOfUpdatesOrDeletes = in.readLong();
    }

    public ReplayChangesReq(final ShardId shardId,
                            String leaderIndex,
                            List<Translog.Operation> changes,
                            long maxSeqNoOfUpdatesOrDeletes) {
        super(shardId);
        this.leaderIndex = leaderIndex;
        this.changes = changes;
        this.maxSeqNoOfUpdatesOrDeletes = maxSeqNoOfUpdatesOrDeletes;
    }

    @Override
    public void writeTo(final StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(leaderIndex);
        out.writeCollection(changes, Translog.Operation::writeOperation);
        out.writeLong(maxSeqNoOfUpdatesOrDeletes);
    }


    @Override
    public String toString() {
        return String.format("ReplayChangesRequest[changes=%s]", changes.size()) ;
    }
}
