package com.phonepe.platform.es.connector.dw.bundle;

import com.google.inject.Stage;
import com.phonepe.plaftorm.es.replicator.commons.job.JobExecutor;
import com.phonepe.platform.es.connector.ESConnectorModule;
import com.phonepe.platform.es.connector.ESREplicationConnector;
import com.phonepe.platform.es.connector.dw.bundle.guice.ReplicationDepsModule;
import com.phonepe.platform.es.connector.sink.Sink;
import com.phonepe.platform.es.connector.store.TranslogCheckpointStore;
import com.phonepe.platform.es.replicator.grpc.events.Events;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.dropwizard.guice.module.installer.feature.health.HealthCheckInstaller;

public class App extends Application<AppConfig> {
    private GuiceBundle guiceBundle;

    @Override
    public void initialize(final Bootstrap<AppConfig> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(true)));

        guiceBundle = GuiceBundle.builder()
                .enableAutoConfig("com.phonepe.platform.es.connector")
                .modules(new ReplicationDepsModule())
                .installers(HealthCheckInstaller.class)
                .printDiagnosticInfo()
                .build(Stage.PRODUCTION);

        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final AppConfig appConfig, final Environment environment) throws Exception {
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
