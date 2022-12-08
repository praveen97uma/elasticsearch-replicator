package com.phonepe.plaftorm.es.replicator.changes.plugin;

import org.elasticsearch.index.engine.EngineConfig;
import org.elasticsearch.index.engine.InternalEngine;
import org.elasticsearch.index.seqno.SequenceNumbers;

import java.io.IOException;

public class ReplicationEngine extends InternalEngine {
    public ReplicationEngine(final EngineConfig engineConfig) {
        super(engineConfig);
    }

    @Override
    protected boolean assertNonPrimaryOrigin(final Operation operation) {
        return true;
    }

    @Override
    protected boolean assertPrimaryIncomingSequenceNumber(final Operation.Origin origin, final long seqNo) {
        assert origin == Operation.Origin.PRIMARY : "Expected origin PRIMARY for replicated ops but was " + origin;
        assert (seqNo != SequenceNumbers.UNASSIGNED_SEQ_NO) : "Expected valid sequence number for replicated op but was unassigned";
        return true;
    }


    @Override
    protected long generateSeqNoForOperationOnPrimary(final Operation operation) {
        if (operation.seqNo() != SequenceNumbers.UNASSIGNED_SEQ_NO) {
            throw new IllegalStateException("Expected valid sequence number for replicate op but was unassigned");
        }

        return operation.seqNo();
    }

    @Override
    protected IndexingStrategy indexingStrategyForOperation(final Index index) throws IOException {
        return planIndexingAsNonPrimary(index);
    }

    @Override
    protected DeletionStrategy deletionStrategyForOperation(final Delete delete) throws IOException {
        return planDeletionAsNonPrimary(delete);
    }
}
