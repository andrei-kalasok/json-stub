package nl.rabobank.powerofattorney.akalasok.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.flyway.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
public class PoaFlywayConfig extends FlywayAutoConfiguration.FlywayConfiguration {

    public PoaFlywayConfig(FlywayProperties properties,
                           DataSourceProperties dataSourceProperties,
                           ResourceLoader resourceLoader,
                           ObjectProvider<DataSource> dataSource,
                           ObjectProvider<DataSource> flywayDataSource,
                           ObjectProvider<FlywayMigrationStrategy> migrationStrategy,
                           ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers,
                           ObjectProvider<Callback> callbacks,
                           @SuppressWarnings("deprecation") ObjectProvider<FlywayCallback> flywayCallbacks) {
        super(properties, dataSourceProperties, resourceLoader, dataSource, flywayDataSource, migrationStrategy,
                fluentConfigurationCustomizers, callbacks, flywayCallbacks);
    }

    @Primary
    @Bean(name = "flywayInitializer")
    @DependsOn("springUtility")
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        return super.flywayInitializer(flyway);
    }
}
