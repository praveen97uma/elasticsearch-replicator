package com.phonepe.plaftorm.es.replicator.changes.plugin.translog;

import lombok.Getter;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.translog.Translog;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Getter
public class ApplyChangesResponse extends ActionResponse {
    private boolean success;

    public ApplyChangesResponse(StreamInput inp) throws IOException {
        success = inp.readBoolean();
    }

    public ApplyChangesResponse(boolean success) {
        this.success = success;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeBoolean(success);
    }
}
