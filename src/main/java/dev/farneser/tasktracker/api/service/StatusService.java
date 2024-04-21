package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.status.create.CreateStatusCommand;
import dev.farneser.tasktracker.api.operations.commands.status.delete.DeleteStatusCommand;
import dev.farneser.tasktracker.api.operations.commands.status.patch.PatchStatusCommand;
import dev.farneser.tasktracker.api.operations.queries.status.getbyid.GetStatusByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.status.getbyuserid.GetStatusByUserIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandstatusid.GetTaskByUserIdAndStatusIdQuery;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.web.dto.status.CreateStatusDto;
import dev.farneser.tasktracker.api.web.dto.status.PatchStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The `StatusService` class provides operations related to statuses, including creation, retrieval,
 * updating, and deletion.
 * It interacts with the mediator to send commands and queries for status-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserService userService;

    /**
     * Creates a new status for the authenticated user.
     *
     * @param dto            The DTO containing information for creating the status.
     * @param authentication The authentication object representing the user.
     * @return The created status view.
     * @throws NotFoundException If the user is not found.
     */
    public StatusView create(CreateStatusDto dto, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.debug("Creating status {}", dto.getStatusName());

        UserView user = userService.getUser(authentication);

        log.debug("Creating status {}", dto.getStatusName());

        CreateStatusCommand command = modelMapper.map(dto, CreateStatusCommand.class);

        log.debug("Creating status {}", dto.getStatusName());

        command.setUserId(user.getId());

        log.debug("Creating status {}", dto.getStatusName());

        Long statusId = mediator.send(command);

        log.debug("Created status {}", dto.getStatusName());

        return get(statusId, authentication);
    }

    /**
     * Retrieves a list of statuses for the authenticated user.
     *
     * @param projectId      The ID of project (-1 to get all)
     * @param retrieveTasks  Flag indicating whether to retrieve tasks along with statuses.
     * @param authentication The authentication object representing the user.
     * @return The list of status views.
     * @throws NotFoundException If the user is not found.
     */
    public List<StatusView> get(Long projectId, Boolean retrieveTasks, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting statuses for user {}", user.getEmail());

        return mediator.send(new GetStatusByUserIdQuery(user.getId(), projectId, retrieveTasks));
    }

    /**
     * Retrieves a specific status for the authenticated user.
     *
     * @param authentication The authentication object representing the user.
     * @return The status view.
     * @throws NotFoundException If the user or status is not found.
     */
    public StatusView get(Long statusId, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetStatusByIdQuery(user.getId(), statusId));
    }

    /**
     * Deletes a specific status for the authenticated user.
     *
     * @param statusId       The ID of the status to delete.
     * @param authentication The authentication object representing the user.
     * @throws NotFoundException If the user or status is not found.
     */
    public void delete(Long statusId, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        mediator.send(new DeleteStatusCommand(user.getId(), statusId));
    }

    /**
     * Updates a specific status for the authenticated user.
     *
     * @param statusId       The id of status
     * @param patchStatusDto The DTO containing information for updating the status.
     * @param authentication The authentication object representing the user.
     * @return The updated status view.
     * @throws NotFoundException If the user or status is not found.
     */
    public StatusView patch(Long statusId, PatchStatusDto patchStatusDto, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        PatchStatusCommand command = modelMapper.map(patchStatusDto, PatchStatusCommand.class);

        command.setStatusId(statusId);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(statusId, authentication);
    }

    /**
     * Retrieves tasks associated with a specific status for the authenticated user.
     *
     * @param statusId       The ID of the status for which to retrieve tasks.
     * @param authentication The authentication object representing the user.
     * @return The list of task lookup views.
     * @throws NotFoundException If the user or status is not found.
     */
    public List<TaskLookupView> getTasks(Long statusId, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting tasks for status {} for user {}", statusId, user.getEmail());

        return mediator.send(new GetTaskByUserIdAndStatusIdQuery(user.getId(), statusId));
    }

    public List<StatusView> get(Boolean retrieveTasks, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        return get(-1L, retrieveTasks, authentication);
    }
}