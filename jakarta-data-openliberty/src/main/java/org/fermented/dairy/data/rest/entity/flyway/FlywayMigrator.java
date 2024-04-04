package org.fermented.dairy.data.rest.entity.flyway;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@ApplicationScoped
public class FlywayMigrator {

    @Resource(name = "jdbc/psDB")
    DataSource dataSource;

    public void postConstruct(@Observes @Initialized(ApplicationScoped.class) Object o) {
        Flyway flyway = new Flyway(Flyway.configure().dataSource(dataSource));
        flyway.migrate();

    }
}
