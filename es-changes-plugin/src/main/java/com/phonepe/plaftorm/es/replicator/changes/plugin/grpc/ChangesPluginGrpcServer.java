package com.phonepe.plaftorm.es.replicator.changes.plugin.grpc;

import ch.qos.logback.classic.Level;
import com.phonepe.plaftorm.es.replicator.changes.plugin.grpc.CDCEngine;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

@Slf4j
public class ChangesPluginGrpcServer extends AbstractLifecycleComponent {
    private Server server;
    private Thread thread;

    private final Injector injector;

    @Inject
    public ChangesPluginGrpcServer(Injector injector) {
        this.injector = injector;
    }


    @SneakyThrows
    @Override
    protected void doStart() {
        ch.qos.logback.classic.Logger nettyLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("io.grpc");
        nettyLogger.setLevel(Level.INFO);

        int port = Integer.parseInt(System.getenv("GRPC_PORT"));
        thread = new Thread(() -> AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                try {
                    server = ServerBuilder.forPort(port)
                            .addService(injector.getInstance(CDCEngine.class))
                            .build()
                            .start();
                    log.info("***************Starting grpc server at port*************: {}", server.getPort());
                    server.awaitTermination();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        }));

        thread.start();
//        server.awaitTermination();
    }

    @Override
    protected void doStop() {
        if (thread != null)
            thread.interrupt();
    }

    @Override
    protected void doClose() throws IOException {

    }

//    public static void main(String... args) {
//        ChangesPluginService service = new ChangesPluginService(injector);
//        service.start();
//    }
}
