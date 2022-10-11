package com.phonepe.plaftorm.es.replicator.commons.lifecycle;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ManagedComponentsRunner {

    private final String handlerPackage;
    private final List<ManagedLifecycle> managedInstances = new ArrayList<>();

    private final ExecutorService executorService;

    private final Injector injector;

    public ManagedComponentsRunner(Injector injector, String handlerPackage) {
        this.handlerPackage = handlerPackage;
        this.injector = injector;
        this.executorService = Executors.newCachedThreadPool();
    }

    private void discover() {
        Reflections reflections = new Reflections(handlerPackage);
        Set<Class<? extends ManagedLifecycle>> subTypes = reflections.getSubTypesOf(ManagedLifecycle.class);

        subTypes.forEach(subType -> manage(injector.getInstance(subType)));
        log.info("Discovered instances {}", managedInstances);
    }

    private void startAll() {
        managedInstances.forEach(instance -> {
            executorService.submit(() -> {
                log.info("Starting: {}", instance.getClass()
                        .getSimpleName());
                try {
                    instance.start();
                } catch (Exception e) {
                    throw new RuntimeException("Unable to start " + instance.getClass().getSimpleName(), e);
                }
            });
        });
    }

    private void stopAll() {
        managedInstances.forEach(instance -> {
            try {
                log.info("Stopping {}", instance.getClass().getSimpleName());
                instance.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void start() {
        discover();
        startAll();
        log.info("Started all managed services");
    }

    public void stop() {
        stopAll();
        log.info("Stopped all managed services");
        if (!executorService.isShutdown()) {
            log.info("Shutting down thread pool running managed instances");
            executorService.shutdownNow();
        }
    }

    public void manage(ManagedLifecycle managed) {
        this.managedInstances.add(managed);
    }
}
