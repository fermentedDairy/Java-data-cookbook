package org.fermented.dairy.data.rest.it.utils;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.time.Duration;

public class LibertyContainer extends GenericContainer<LibertyContainer> {

    public LibertyContainer(final ImageFromDockerfile image, final int httpPort) {

        super(image);
        addExposedPorts(httpPort);

        // wait for smarter planet message by default
        waitingFor(Wait.forLogMessage("^.*CWWKF0011I.*$", 1).withStartupTimeout(Duration.ofSeconds(120)))
                .withLogConsumer(
                new Slf4jLogConsumer(LoggerFactory.getLogger(LibertyContainer.class)));
    }

    public String getBaseURL() throws IllegalStateException {
        return "http://" + getHost() + ":" + getFirstMappedPort();
    }

}
