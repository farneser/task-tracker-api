package dev.farneser.tasktracker.api.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
public class TestContainerConfig {
    private PostgreSQLContainer<?> pgContainer;

    @Bean
    public DataSource dataSource() {
        pgContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("task-tracker")
                .withUsername("postgres")
                .withPassword("postgres");

        pgContainer.start();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(pgContainer.getJdbcUrl());
        dataSource.setUsername(pgContainer.getUsername());
        dataSource.setPassword(pgContainer.getPassword());

        return dataSource;
    }


    @PreDestroy
    public void cleanUp() {
        pgContainer.close();
    }
}

