package com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay;

import com.phonepe.platform.es.replicator.models.ApplyTranslogResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.support.WriteResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

@Getter
@Setter
@ToString
public class ReplayChangesResponse extends ReplicationResponse implements WriteResponse {
    private boolean success;

    private long lastSuccessfulSeqNo;

    private ApplyTranslogResponse.ErrorCode errorCode;

    public ReplayChangesResponse() {
        super();
    }


    public ReplayChangesResponse(StreamInput inp) throws IOException {
        super();
        readFrom(inp);
    }

    @Override
    public void readFrom(final StreamInput in) throws IOException {
        super.readFrom(in);
        this.success = in.readBoolean();
        this.lastSuccessfulSeqNo = in.readLong();
        String errorCode = in.readOptionalString();
        if (errorCode != null) {
            this.errorCode = ApplyTranslogResponse.ErrorCode.valueOf(errorCode);
        }
    }

    @Override
    public void writeTo(final StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeBoolean(success);
        out.writeLong(lastSuccessfulSeqNo);
        out.writeOptionalString(errorCode.name());
    }

    @Override
    public void setForcedRefresh(final boolean forcedRefresh) {

    }
}
