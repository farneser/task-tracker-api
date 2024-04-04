package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
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
import dev.farneser.tasktracker.api.web.dto.status.CreateStatusDto;
import dev.farneser.tasktracker.api.web.dto.status.PatchStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The `ColumnService` class provides operations related to columns, including creation, retrieval,
 * updating, and deletion.
 * It interacts with the mediator to send commands and queries for column-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserService userService;

    /**
     * Creates a new column for the authenticated user.
     *
     * @param dto            The DTO containing information for creating the column.
     * @param authentication The authentication object representing the user.
     * @return The created column view.
     * @throws NotFoundException If the user is not found.
     */
    public StatusView create(CreateStatusDto dto, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        log.debug("Creating column {}", dto.getStatusName());

        UserView user = userService.getUser(authentication);

        log.debug("Creating column {}", dto.getStatusName());

        CreateStatusCommand command = modelMapper.map(dto, CreateStatusCommand.class);

        log.debug("Creating column {}", dto.getStatusName());

        command.setUserId(user.getId());

        log.debug("Creating column {}", dto.getStatusName());

        Long statusId = mediator.send(command);

        log.debug("Created column {}", dto.getStatusName());

        return get(statusId, authentication);
    }

    /**
     * Retrieves a list of columns for the authenticated user.
     *
     * @param projectId      The ID of project (-1 to get all)
     * @param retrieveTasks  Flag indicating whether to retrieve tasks along with columns.
     * @param authentication The authentication object representing the user.
     * @return The list of column views.
     * @throws NotFoundException If the user is not found.
     */
    public List<StatusView> get(Long projectId, Boolean retrieveTasks, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting columns for user {}", user.getEmail());

        return mediator.send(new GetStatusByUserIdQuery(user.getId(), projectId, retrieveTasks));
    }

    /**
     * Retrieves a specific column for the authenticated user.
     *
     * @param authentication The authentication object representing the user.
     * @return The column view.
     * @throws NotFoundException If the user or column is not found.
     */
    public StatusView get(Long statusId, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetStatusByIdQuery(user.getId(), statusId));
    }

    /**
     * Deletes a specific column for the authenticated user.
     *
     * @param statusId       The ID of the status to delete.
     * @param authentication The authentication object representing the user.
     * @throws NotFoundException If the user or column is not found.
     */
    public void delete(Long statusId, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        mediator.send(new DeleteStatusCommand(user.getId(), statusId));
    }

    /**
     * Updates a specific column for the authenticated user.
     *
     * @param statusId       The id of status
     * @param patchStatusDto The DTO containing information for updating the column.
     * @param authentication The authentication object representing the user.
     * @return The updated column view.
     * @throws NotFoundException If the user or column is not found.
     */
    public StatusView patch(Long statusId, PatchStatusDto patchStatusDto, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        PatchStatusCommand command = modelMapper.map(patchStatusDto, PatchStatusCommand.class);

        command.setStatusId(statusId);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(statusId, authentication);
    }

    /**
     * Retrieves tasks associated with a specific column for the authenticated user.
     *
     * @param statusId       The ID of the column for which to retrieve tasks.
     * @param authentication The authentication object representing the user.
     * @return The list of task lookup views.
     * @throws NotFoundException If the user or column is not found.
     */
    public List<TaskLookupView> getTasks(Long statusId, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting tasks for column {} for user {}", statusId, user.getEmail());

        return mediator.send(new GetTaskByUserIdAndStatusIdQuery(user.getId(), statusId));
    }

    public List<StatusView> get(Boolean retrieveTasks, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        return get(-1L, retrieveTasks, authentication);
    }
}