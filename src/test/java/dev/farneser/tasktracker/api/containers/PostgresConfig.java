package dev.farneser.tasktracker.api.containers;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@Configuration
@Profile("test")
public abstract class PostgresConfig {

    @Bean("pg-container")
    public GenericContainer<?> postgresContainer() {
        var postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("task-tracker")
                .withExposedPorts(5432)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                        new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(127), new ExposedPort(5432)))));

        postgresContainer.start();

        return postgresContainer;
    }
}
