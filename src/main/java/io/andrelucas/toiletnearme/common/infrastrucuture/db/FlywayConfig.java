package io.andrelucas.toiletnearme.common.infrastrucuture.db;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(@Qualifier("clientDataSource") final DataSource dataSource) {
        final var configuration = new ClassicConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setLocations(new Location("classpath:db/migration"));
        configuration.setBaselineVersion("0");
        configuration.setBaselineOnMigrate(true);

        final var flyway = new Flyway(configuration);
        flyway.migrate();

        return flyway;
    }
}
