package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.create.CreateProjectInviteTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.delete.DeleteProjectInviteTokenCommand;
import dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyid.GetProjectInviteTokenByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyprojectid.GetProjectInviteTokenByProjectIdQuery;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectInviteTokenService {
    private final UserService userService;
    private final Mediator mediator;

    private ProjectInviteTokenView getById(Long id, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectInviteTokenByIdQuery(user.getId(), id));
    }

    public ProjectInviteTokenView get(Long projectId, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectInviteTokenByProjectIdQuery(user.getId(), projectId));
    }

    public ProjectInviteTokenView create(Long projectId, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        CreateProjectInviteTokenCommand command = new CreateProjectInviteTokenCommand(user.getId(), projectId);

        Long tokenId = mediator.send(command);

        return getById(tokenId, authentication);
    }

    public void delete(Long projectId, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        new DeleteProjectInviteTokenCommand(user.getId(), projectId);
    }
}
