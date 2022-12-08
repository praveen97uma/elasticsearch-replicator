package com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay;

import org.elasticsearch.action.ActionType;

public class ReplayChangesAction extends ActionType<ReplayChangesResponse> {
    public static final String NAME =  "indices:data/write/plugins/replication/changes";
    public static final ReplayChangesAction INSTANCE = new ReplayChangesAction();
    public ReplayChangesAction() {
        super(NAME, ReplayChangesResponse::new);
    }
}
