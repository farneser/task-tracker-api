package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.accept.AcceptProjectInviteTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.create.CreateProjectInviteTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.delete.DeleteProjectInviteTokenCommand;
import dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyid.GetProjectInviteTokenByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyprojectid.GetProjectInviteTokenByProjectIdQuery;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class for managing project invite tokens.
 * This service provides functionality to create, retrieve, and delete project invite tokens,
 * as well as accepting project invitations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectInviteTokenService {
    private final UserService userService;
    private final Mediator mediator;

    /**
     * Retrieves a project invite token by its ID.
     *
     * @param id             The ID of the project invite token to retrieve.
     * @param authentication The authentication object representing the user's credentials.
     * @return The project invite token view.
     * @throws NotFoundException               If the project invite token is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    private ProjectInviteTokenView getById(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectInviteTokenByIdQuery(user.getId(), id));
    }

    /**
     * Retrieves a project invite token by project ID.
     *
     * @param projectId      The ID of the project to retrieve the invite token for.
     * @param authentication The authentication object representing the user's credentials.
     * @return The project invite token view.
     * @throws NotFoundException               If the project invite token is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public ProjectInviteTokenView get(Long projectId, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectInviteTokenByProjectIdQuery(user.getId(), projectId));
    }

    /**
     * Creates a new project invite token for the specified project.
     *
     * @param projectId      The ID of the project to create the invite token for.
     * @param authentication The authentication object representing the user's credentials.
     * @return The project invite token view.
     * @throws NotFoundException               If the project invite token is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public ProjectInviteTokenView create(Long projectId, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        CreateProjectInviteTokenCommand command = new CreateProjectInviteTokenCommand(user.getId(), projectId);

        Long tokenId = mediator.send(command);

        return getById(tokenId, authentication);
    }

    /**
     * Deletes the project invite token associated with the specified project.
     *
     * @param projectId      The ID of the project to delete the invite token for.
     * @param authentication The authentication object representing the user's credentials.
     * @throws NotFoundException               If the project invite token is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public void delete(Long projectId, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        mediator.send(new DeleteProjectInviteTokenCommand(user.getId(), projectId));
    }

    /**
     * Accepts a project invitation using the specified invite token.
     *
     * @param token          The invite token to accept the project invitation.
     * @param authentication The authentication object representing the user's credentials.
     * @throws NotFoundException               If the project invite token is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public void accept(String token, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {

        UserView user = userService.getUser(authentication);

        mediator.send(new AcceptProjectInviteTokenCommand(user.getId(), token));
    }
}
