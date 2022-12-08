package com.phonepe.plaftorm.es.replicator.changes.plugin.translog;

import org.elasticsearch.action.ActionType;

public class ApplyChangesAction extends ActionType<ApplyChangesResponse> {
    public static ApplyChangesAction INSTANCE = new ApplyChangesAction();
    public static final String NAME = "shards/replication/changes/apply";

    public ApplyChangesAction() {
        super(NAME, ApplyChangesResponse::new);
    }
}
