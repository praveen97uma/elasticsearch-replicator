package com.phonepe.plaftorm.es.replicator.changes.plugin.actions;

import org.elasticsearch.action.ActionType;
import org.elasticsearch.common.io.stream.Writeable;

public class GetChangesAction extends ActionType<GetChangesResponse> {
    public static GetChangesAction INSTANCE = new GetChangesAction();
    public static final String NAME = "indices/replication/changes";

    public GetChangesAction() {
        super(NAME, GetChangesResponse::new);
    }
}
