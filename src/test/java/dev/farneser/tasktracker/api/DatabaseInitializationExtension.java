package dev.farneser.tasktracker.api;

import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.permissions.Role;
import dev.farneser.tasktracker.api.repository.ProjectRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatabaseInitializationExtension implements BeforeAllCallback, ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static UserRepository userRepository;
    private static ProjectRepository projectRepository;
    private static StatusRepository statusRepository;
    private static TaskRepository taskRepository;
    private static Boolean isStarts = false;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) {
        applicationContext = context;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        userRepository = applicationContext.getBean(UserRepository.class);
        projectRepository = applicationContext.getBean(ProjectRepository.class);
        statusRepository = applicationContext.getBean(StatusRepository.class);
        taskRepository = applicationContext.getBean(TaskRepository.class);

        if (!isStarts) {
            initUsers();
        }

        isStarts = true;
    }

    private void initUsers() {

        User user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("password1")
                .isSubscribed(true)
                .registerDate(new Date())
                .isEnabled(true)
                .isLocked(false)
                .role(Role.USER)
                .build();

        userRepository.save(user1);

        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("password2")
                .isSubscribed(true)
                .registerDate(new Date())
                .isEnabled(true)
                .isLocked(false)
                .role(Role.USER)
                .build();

        userRepository.save(user2);

        User user3 = User.builder()
                .username("user3")
                .email("user3@example.com")
                .password("password3")
                .isSubscribed(true)
                .registerDate(new Date())
                .isEnabled(true)
                .isLocked(false)
                .role(Role.USER)
                .build();

        userRepository.save(user3);
    }
}