package dev.farneser.tasktracker.api.config;

import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class DatabaseLoader {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteTokenRepository projectInviteTokenRepository;
    private final StatusRepository statusRepository;
    private final TaskRepository taskRepository;
    private final Mediator mediator;

    @Bean
    public void run() {
        boolean isReposEmpty = isRepositoryEmpty(
                userRepository,
                projectRepository,
                projectMemberRepository,
                projectInviteTokenRepository,
                statusRepository,
                taskRepository
        );

        if (!isReposEmpty) {
            log.info("Database is already populated with data and does not require additional row additions. Adding operations are rejected in development mode.");

            return;
        }

        initUsers();
    }

    private static boolean isRepositoryEmpty(CrudRepository<?, ?>... repos) {

        for (CrudRepository<?, ?> repo : repos) {
            if (repo.count() != 0) {
                return false;
            }
        }

        return true;
    }

    @SneakyThrows
    private void initUsers() {

        RegisterUserCommand reg1 = new RegisterUserCommand();
        reg1.setUsername("user1");
        reg1.setEmail("user1@example.com");
        reg1.setPassword("password1");
        reg1.setConfirmPassword("password1");
        reg1.setSubscribed(true);

        mediator.send(reg1);

        RegisterUserCommand reg2 = new RegisterUserCommand();
        reg2.setUsername("user2");
        reg2.setEmail("user2@example.com");
        reg2.setPassword("password2");
        reg2.setConfirmPassword("password2");
        reg2.setSubscribed(false);

        mediator.send(reg2);

        RegisterUserCommand reg3 = new RegisterUserCommand();
        reg3.setUsername("user3");
        reg3.setEmail("user3@example.com");
        reg3.setPassword("password3");
        reg3.setConfirmPassword("password3");
        reg3.setSubscribed(true);

        mediator.send(reg3);
    }
}
