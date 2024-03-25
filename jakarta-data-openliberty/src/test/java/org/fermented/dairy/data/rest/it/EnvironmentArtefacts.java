package org.fermented.dairy.data.rest.it;

import org.fermented.dairy.data.rest.it.utils.LibertyContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public record EnvironmentArtefacts(LibertyContainer app, PostgreSQLContainer db) {
    public void start() {
        db.start();
        app.start();
    }

    public void stop() {
        app.stop();
        db.stop();
    }
}
