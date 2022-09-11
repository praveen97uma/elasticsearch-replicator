package com.phonepe.plaftorm.es.replicator.changes.plugin.actions;

import lombok.Getter;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.translog.Translog;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Getter
public class GetChangesResponse extends ActionResponse {
    private Collection<Translog.Operation> changes;
    private long fromSeqNo;
    private long  maxSeqNoOfUpdatesOrDeletes;
    private long lastSyncedGlobalCheckpoint;

    public GetChangesResponse(StreamInput inp) throws IOException {
        this.changes = inp.readList(Translog.Operation::readOperation);
        this.fromSeqNo = inp.readVLong();
        this.maxSeqNoOfUpdatesOrDeletes = inp.readLong();
        this.lastSyncedGlobalCheckpoint = inp.readLong();
    }

    public GetChangesResponse(List<Translog.Operation> operations, long fromSeqNo, long maxSeqNoOfUpdatesOrDeletes, long lastSyncedGlobalCheckpoint) {
        this.changes = operations;
        this.fromSeqNo = fromSeqNo;
        this.maxSeqNoOfUpdatesOrDeletes = maxSeqNoOfUpdatesOrDeletes;
        this.lastSyncedGlobalCheckpoint = lastSyncedGlobalCheckpoint;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeCollection(changes, Translog.Operation::writeOperation);
        out.writeLong(fromSeqNo);
        out.writeLong(maxSeqNoOfUpdatesOrDeletes);
        out.writeLong(lastSyncedGlobalCheckpoint);
    }
}
