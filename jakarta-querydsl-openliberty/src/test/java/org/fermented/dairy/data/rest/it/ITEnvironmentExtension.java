package org.fermented.dairy.data.rest.it;

import org.fermented.dairy.data.rest.it.utils.LibertyContainer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ITEnvironmentExtension implements Extension, AfterAllCallback, BeforeAllCallback {

    private static final Network network = Network.newNetwork();

    private static EnvironmentArtefacts environmentArtefacts = null;

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        lock.writeLock().lock();
        try {
            if (environmentArtefacts != null) {
                throw new IllegalStateException("Must be called once");
            }
            final PostgreSQLContainer db = new PostgreSQLContainer<>("postgres:16.2")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("testPassword")
                    .withNetworkAliases("db")
                    .withNetwork(network);

            final ImageFromDockerfile imageFromDockerfile = new ImageFromDockerfile()
                    .withDockerfile(Paths.get("./Dockerfile"));

            final LibertyContainer app = new LibertyContainer(imageFromDockerfile, 9080)
                    .dependsOn(db)
                    .withEnv(Map.of("DB_SERVER_NAME", "db"))
                    .waitingFor(new HttpWaitStrategy()
                            .forPath("/health")
                            .forPort(9080)
                            .forStatusCode(200)
                            .withStartupTimeout(Duration.ofSeconds(240)))
                    .withNetwork(network);

            environmentArtefacts = new EnvironmentArtefacts(app, db);
            environmentArtefacts.start();

        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void afterAll(final ExtensionContext context) throws Exception {
        lock.readLock().lock();
        try {
            if (environmentArtefacts == null) {
                throw new IllegalStateException("Must have been called");
            }
            environmentArtefacts.stop();
        } finally {
            lock.readLock().unlock();
        }

    }

    public static LibertyContainer app() {
        return environmentArtefacts.app();
    }

    public static PostgreSQLContainer db() {
        return environmentArtefacts.db();
    }
}
