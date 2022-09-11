package com.phonepe.plaftorm.es.replicator.changes.plugin;

import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.GetChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.actions.TransportGetChangesAction;
import com.phonepe.plaftorm.es.replicator.changes.plugin.rest.GetIndexMetadata;
import com.phonepe.plaftorm.es.replicator.changes.plugin.rest.GetShardHistoryOperations;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.PersistentTaskPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ChangesPlugin extends Plugin implements ActionPlugin, PersistentTaskPlugin {
    @Override
    public List<RestHandler> getRestHandlers(Settings settings, RestController restController, ClusterSettings clusterSettings, IndexScopedSettings indexScopedSettings, SettingsFilter settingsFilter, IndexNameExpressionResolver indexNameExpressionResolver, Supplier<DiscoveryNodes> nodesInCluster) {
        return Arrays.asList(new GetIndexMetadata(settings, restController), new GetShardHistoryOperations(settings, restController));
    }

    @Override
    public List<ActionHandler<? extends ActionRequest, ? extends ActionResponse>> getActions() {
        return Arrays.asList(
             new ActionHandler<>(GetChangesAction.INSTANCE, TransportGetChangesAction.class)
        );
    }
}
