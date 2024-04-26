package dev.farneser.tasktracker.api;

import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.permissions.Role;
import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.repository.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Configuration
public class DatabaseInitializationExtension implements BeforeAllCallback, ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static UserRepository userRepository;
    private static ProjectRepository projectRepository;
    private static ProjectMemberRepository projectMemberRepository;
    private static StatusRepository statusRepository;
    private static TaskRepository taskRepository;
    private static PasswordEncoder passwordEncoder;
    private static Boolean isStarts = false;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) {
        applicationContext = context;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        userRepository = applicationContext.getBean(UserRepository.class);
        projectRepository = applicationContext.getBean(ProjectRepository.class);
        projectMemberRepository = applicationContext.getBean(ProjectMemberRepository.class);
        statusRepository = applicationContext.getBean(StatusRepository.class);
        taskRepository = applicationContext.getBean(TaskRepository.class);
        passwordEncoder = applicationContext.getBean(PasswordEncoder.class);

        if (!isStarts) {
            initUsers();
            initProjects();
        }

        isStarts = true;
    }

    private void initUsers() {

        User user1 = User.builder()
                .username("user1")
                .email("user1@builder.com")
                .password(passwordEncoder.encode("password1"))
                .isSubscribed(true)
                .registerDate(new Date())
                .isEnabled(true)
                .isLocked(false)
                .role(Role.USER)
                .build();

        userRepository.save(user1);

        User user2 = User.builder()
                .username("user2")
                .email("user2@builder.com")
                .password(passwordEncoder.encode("password2"))
                .isSubscribed(true)
                .registerDate(new Date())
                .isEnabled(true)
                .isLocked(false)
                .role(Role.USER)
                .build();

        userRepository.save(user2);

        User user3 = User.builder()
                .username("user3")
                .email("user3@builder.com")
                .password(passwordEncoder.encode("password3"))
                .isSubscribed(true)
                .registerDate(new Date())
                .isEnabled(true)
                .isLocked(false)
                .role(Role.USER)
                .build();

        userRepository.save(user3);
    }

    private void initProjects() {

        Project project1 = Project
                .builder()
                .projectName("Project 1")
                .build();

        projectRepository.save(project1);

        Project project2 = Project
                .builder()
                .projectName("Project 2")
                .build();

        projectRepository.save(project2);

        Project project3 = Project
                .builder()
                .projectName("Project 3")
                .build();

        projectRepository.save(project3);

        User user1 = userRepository.findByEmail("user1@builder.com").orElseThrow();
        User user2 = userRepository.findByEmail("user2@builder.com").orElseThrow();
        User user3 = userRepository.findByEmail("user3@builder.com").orElseThrow();

        ProjectMember member1 = ProjectMember
                .builder()
                .member(user1)
                .project(project1)
                .build();

        projectMemberRepository.save(member1);

        ProjectMember member2 = ProjectMember
                .builder()
                .member(user2)
                .project(project1)
                .build();

        projectMemberRepository.save(member2);

        ProjectMember member3 = ProjectMember
                .builder()
                .member(user2)
                .project(project2)
                .build();

        projectMemberRepository.save(member3);

        ProjectMember member4 = ProjectMember
                .builder()
                .member(user3)
                .project(project2)
                .build();

        projectMemberRepository.save(member4);

        ProjectMember member5 = ProjectMember
                .builder()
                .member(user1)
                .project(project3)
                .build();

        projectMemberRepository.save(member5);

        ProjectMember member6 = ProjectMember
                .builder()
                .member(user2)
                .project(project3)
                .build();

        projectMemberRepository.save(member6);

        ProjectMember member7 = ProjectMember
                .builder()
                .member(user3)
                .project(project3)
                .build();

        projectMemberRepository.save(member7);
    }
}