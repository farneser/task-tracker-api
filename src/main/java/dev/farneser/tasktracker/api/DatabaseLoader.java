package dev.farneser.tasktracker.api;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.project.create.CreateProjectCommand;
import dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.accept.AcceptProjectInviteTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.create.CreateProjectInviteTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.status.create.CreateStatusCommand;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.operations.queries.project.getbyid.GetProjectByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyid.GetProjectInviteTokenByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.status.getbyid.GetStatusByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyid.GetUserByIdQuery;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev")
public class DatabaseLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteTokenRepository projectInviteTokenRepository;
    private final StatusRepository statusRepository;
    private final TaskRepository taskRepository;
    private final Mediator mediator;

    /**
     * Checks if the repositories are empty and initializes the database if needed.
     *
     * @param args The application arguments
     * @throws Exception if an error occurs during database initialization
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
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
        } else {
            log.info("Database is empty. Initializing...");
        }

        initDatabase();
    }

    /**
     * Checks if the given repositories are empty.
     *
     * @param repos Array of CrudRepositories to check
     * @return true if all repositories are empty, otherwise false
     */
    private static boolean isRepositoryEmpty(CrudRepository<?, ?>... repos) {

        for (CrudRepository<?, ?> repo : repos) {
            if (repo.count() != 0) {
                return false;
            }
        }

        return true;
    }

    private UserView createUser(String username)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        RegisterUserCommand command = new RegisterUserCommand();

        command.setUsername(username);
        command.setEmail(username + "@example.com");
        command.setPassword("password");
        command.setConfirmPassword("password");
        command.setSubscribed(true);

        return mediator.send(new GetUserByIdQuery(mediator.send(command)));
    }

    private ProjectView createProject(Long userId)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        CreateProjectCommand command = new CreateProjectCommand();

        command.setCreatorId(userId);
        command.setProjectName("User " + userId + "'s project");

        return mediator.send(new GetProjectByIdQuery(userId, mediator.send(command)));
    }

    private ProjectInviteTokenView createInviteToken(Long userId, Long projectId)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        Long id = mediator.send(new CreateProjectInviteTokenCommand(userId, projectId));

        return mediator.send(new GetProjectInviteTokenByIdQuery(userId, id));
    }

    private void addUserToProject(Long userId, String token)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        mediator.send(new AcceptProjectInviteTokenCommand(userId, token));
    }

    private StatusView createStatus(Long userId, Long projectId, String statusName, Boolean isCompleted)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        Long id = mediator.send(new CreateStatusCommand(userId, projectId, statusName, "#ffffff", isCompleted));

        return mediator.send(new GetStatusByIdQuery(userId, id));
    }

    /**
     * Initializes the database with sample users, projects, and invite tokens.
     *
     * @throws NotFoundException               if a requested resource is not found
     * @throws OperationNotAuthorizedException if an operation is not authorized
     */
    private void initDatabase() throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.info("Initializing sample users...");

        UserView user1 = createUser("user1");
        UserView user2 = createUser("user2");
        UserView user3 = createUser("user3");

        log.info("Sample users created.");

        log.info("Initializing sample projects...");

        ProjectView project1 = createProject(user1.getId());
        ProjectView project2 = createProject(user2.getId());
        ProjectView project3 = createProject(user3.getId());

        log.info("Sample projects created.");

        log.info("Initializing sample invite tokens...");

        ProjectInviteTokenView pitView1 = createInviteToken(user1.getId(), project1.getId());
        ProjectInviteTokenView pitView2 = createInviteToken(user2.getId(), project2.getId());
        ProjectInviteTokenView pitView3 = createInviteToken(user3.getId(), project3.getId());

        log.info("Sample invite tokens created.");

        log.info("Adding users to projects...");

        addUserToProject(user2.getId(), pitView1.getToken());
        addUserToProject(user1.getId(), pitView3.getToken());
        addUserToProject(user2.getId(), pitView3.getToken());

        log.info("Users added to projects.");

        log.info("Database initialization complete.");
    }
}

