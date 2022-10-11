package com.phonepe.plaftorm.es.replicator.changes.plugin.injection;

import org.elasticsearch.common.inject.AbstractModule;

public class CDCModule extends AbstractModule {

    @Override
    protected void configure() {
        requestStaticInjection(Dependencies.class);
    }
}
