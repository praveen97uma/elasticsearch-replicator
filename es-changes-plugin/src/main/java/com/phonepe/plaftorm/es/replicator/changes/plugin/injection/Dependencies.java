package com.phonepe.plaftorm.es.replicator.changes.plugin.injection;

import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.indices.IndicesService;

public class Dependencies {


    @Inject
    public static IndicesService indicesService;

    @Inject
    public static ClusterService clusterService;
}
