package dev.farneser.tasktracker.api.config;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
public class TestContainerConfig {
    @Bean
    public DataSource dataSource() {
        var pgContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("task-tracker")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(new HostConfig()
                        .withPortBindings(new PortBinding(Ports.Binding.bindPort(15432), new ExposedPort(5432))))
                );

        pgContainer.start();

        var dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(pgContainer.getJdbcUrl());
        dataSource.setUsername(pgContainer.getUsername());
        dataSource.setPassword(pgContainer.getPassword());

        return dataSource;
    }
}

