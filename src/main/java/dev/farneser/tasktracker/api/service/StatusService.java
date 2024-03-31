package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.status.create.CreateStatusCommand;
import dev.farneser.tasktracker.api.operations.commands.status.delete.DeleteStatusCommand;
import dev.farneser.tasktracker.api.operations.commands.status.patch.PatchStatusCommand;
import dev.farneser.tasktracker.api.operations.queries.status.getbyid.GetStatusByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.status.getbyuserid.GetStatusByUserIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandcolumnid.GetTaskByUserIdAndColumnIdQuery;
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
     * @param retrieveTasks  Flag indicating whether to retrieve tasks along with columns.
     * @param authentication The authentication object representing the user.
     * @return The list of column views.
     * @throws NotFoundException If the user is not found.
     */
    public List<StatusView> get(Boolean retrieveTasks, Authentication authentication) throws NotFoundException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting columns for user {}", user.getEmail());

        return mediator.send(new GetStatusByUserIdQuery(user.getId(), retrieveTasks));
    }

    /**
     * Retrieves a specific column for the authenticated user.
     *
     * @param id             The ID of the column to retrieve.
     * @param authentication The authentication object representing the user.
     * @return The column view.
     * @throws NotFoundException If the user or column is not found.
     */
    public StatusView get(Long id, Authentication authentication) throws NotFoundException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting column {} for user {}", id, user.getEmail());

        return mediator.send(new GetStatusByIdQuery(user.getId(), id));
    }

    /**
     * Deletes a specific column for the authenticated user.
     *
     * @param id             The ID of the column to delete.
     * @param authentication The authentication object representing the user.
     * @throws NotFoundException If the user or column is not found.
     */
    public void delete(Long id, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Deleting column {} for user {}", id, user.getEmail());

        mediator.send(new DeleteStatusCommand(user.getId(), id));
    }

    /**
     * Updates a specific column for the authenticated user.
     *
     * @param projectId      The id of project
     * @param statusId       The id of status
     * @param patchStatusDto The DTO containing information for updating the column.
     * @param authentication The authentication object representing the user.
     * @return The updated column view.
     * @throws NotFoundException If the user or column is not found.
     */
    public StatusView patch(Long projectId, Long statusId, PatchStatusDto patchStatusDto, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        PatchStatusCommand command = modelMapper.map(patchStatusDto, PatchStatusCommand.class);

        command.setProjectId(projectId);
        command.setStatusId(statusId);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(projectId, authentication);
    }

    /**
     * Retrieves tasks associated with a specific column for the authenticated user.
     *
     * @param id             The ID of the column for which to retrieve tasks.
     * @param authentication The authentication object representing the user.
     * @return The list of task lookup views.
     * @throws NotFoundException If the user or column is not found.
     */
    public List<TaskLookupView> getTasks(Long id, Authentication authentication) throws NotFoundException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting tasks for column {} for user {}", id, user.getEmail());

        return mediator.send(new GetTaskByUserIdAndColumnIdQuery(user.getId(), id));
    }
}