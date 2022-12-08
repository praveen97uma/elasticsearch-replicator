package com.phonepe.plaftorm.es.replicator.changes.plugin;

import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.changes.GetChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.changes.TransportGetChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay.ReplayChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.replay.TransportReplayChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.injection.CDCModule;
import com.phonepe.plaftorm.es.replicator.changes.plugin.rest.ApplyShardHistoryOperations;
import com.phonepe.plaftorm.es.replicator.changes.plugin.rest.GetIndexMetadata;
import com.phonepe.plaftorm.es.replicator.changes.plugin.rest.GetShardChangeOperations;
import com.phonepe.plaftorm.es.replicator.changes.plugin.translog.ApplyChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.translog.TransportApplyChangesAction;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.engine.EngineFactory;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.EnginePlugin;
import org.elasticsearch.plugins.PersistentTaskPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;

import java.util.*;
import java.util.function.Supplier;


public class ChangesPlugin extends Plugin implements ActionPlugin, PersistentTaskPlugin, EnginePlugin {
    @Override
    public List<RestHandler> getRestHandlers(Settings settings, RestController restController, ClusterSettings clusterSettings, IndexScopedSettings indexScopedSettings, SettingsFilter settingsFilter, IndexNameExpressionResolver indexNameExpressionResolver, Supplier<DiscoveryNodes> nodesInCluster) {
        return Arrays.asList(
                new GetIndexMetadata(settings, restController),
                new GetShardChangeOperations(settings, restController),
                new ApplyShardHistoryOperations(settings, restController));
    }

    @Override
    public Collection<Class<? extends LifecycleComponent>> getGuiceServiceClasses() {
        return List.of();
    }
    @Override
    public List<ActionHandler<? extends ActionRequest, ? extends ActionResponse>> getActions() {
        return Arrays.asList(
             new ActionHandler<>(GetChangesAction.INSTANCE, TransportGetChangesAction.class),
             new ActionHandler<>(ApplyChangesAction.INSTANCE, TransportApplyChangesAction.class),
             new ActionHandler<>(ReplayChangesAction.INSTANCE, TransportReplayChangesAction.class)
        );
    }

    @Override
    public List<NamedXContentRegistry.Entry> getNamedXContent() {
        return super.getNamedXContent();
    }

    @Override
    public Collection<Module> createGuiceModules() {
        return Collections.singletonList(new CDCModule());
    }

    @Override
    public Optional<EngineFactory> getEngineFactory(final IndexSettings indexSettings) {
        return Optional.empty();
//        return Optional.of(ReplicationEngine::new);
    }
}
