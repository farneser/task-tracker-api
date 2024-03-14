package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.column.create.CreateColumnCommand;
import dev.farneser.tasktracker.api.operations.commands.column.delete.DeleteColumnCommand;
import dev.farneser.tasktracker.api.operations.commands.column.patch.PatchColumnCommand;
import dev.farneser.tasktracker.api.operations.queries.column.getbyid.GetColumnByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.column.getbyuserid.GetColumnByUserIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandcolumnid.GetTaskByUserIdAndColumnIdQuery;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.web.dto.column.CreateColumnDto;
import dev.farneser.tasktracker.api.web.dto.column.PatchColumnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The `ColumnService` class provides operations related to columns, including creation, retrieval, updating, and deletion.
 * It interacts with the mediator to send commands and queries for column-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnService {
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
    public ColumnView create(CreateColumnDto dto, Authentication authentication) throws NotFoundException {
        log.debug("Creating column {}", dto.getColumnName());

        UserView user = userService.getUser(authentication);

        log.debug("Creating column {}", dto.getColumnName());

        CreateColumnCommand command = modelMapper.map(dto, CreateColumnCommand.class);

        log.debug("Creating column {}", dto.getColumnName());

        command.setUserId(user.getId());

        log.debug("Creating column {}", dto.getColumnName());

        Long columnId = mediator.send(command);

        log.debug("Created column {}", dto.getColumnName());

        return get(columnId, authentication);
    }

    /**
     * Retrieves a list of columns for the authenticated user.
     *
     * @param retrieveTasks  Flag indicating whether to retrieve tasks along with columns.
     * @param authentication The authentication object representing the user.
     * @return The list of column views.
     * @throws NotFoundException If the user is not found.
     */
    public List<ColumnView> get(Boolean retrieveTasks, Authentication authentication) throws NotFoundException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting columns for user {}", user.getEmail());

        return mediator.send(new GetColumnByUserIdQuery(user.getId(), retrieveTasks));
    }

    /**
     * Retrieves a specific column for the authenticated user.
     *
     * @param id             The ID of the column to retrieve.
     * @param authentication The authentication object representing the user.
     * @return The column view.
     * @throws NotFoundException If the user or column is not found.
     */
    public ColumnView get(Long id, Authentication authentication) throws NotFoundException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting column {} for user {}", id, user.getEmail());

        return mediator.send(new GetColumnByIdQuery(user.getId(), id));
    }

    /**
     * Deletes a specific column for the authenticated user.
     *
     * @param id             The ID of the column to delete.
     * @param authentication The authentication object representing the user.
     * @throws NotFoundException If the user or column is not found.
     */
    public void delete(Long id, Authentication authentication) throws NotFoundException {
        UserView user = userService.getUser(authentication);

        log.debug("Deleting column {} for user {}", id, user.getEmail());

        mediator.send(new DeleteColumnCommand(user.getId(), id));
    }

    /**
     * Updates a specific column for the authenticated user.
     *
     * @param id             The ID of the column to update.
     * @param patchColumnDto The DTO containing information for updating the column.
     * @param authentication The authentication object representing the user.
     * @return The updated column view.
     * @throws NotFoundException If the user or column is not found.
     */
    public ColumnView patch(Long id, PatchColumnDto patchColumnDto, Authentication authentication) throws NotFoundException {
        UserView user = userService.getUser(authentication);

        log.debug("Patching column {} for user {}", id, user.getEmail());

        PatchColumnCommand command = modelMapper.map(patchColumnDto, PatchColumnCommand.class);

        log.debug("Patching column {} for user {}", id, user.getEmail());

        command.setColumnId(id);
        command.setUserId(user.getId());

        log.debug("Patching column {} for user {}", id, user.getEmail());

        mediator.send(command);

        log.debug("Patched column {} for user {}", id, user.getEmail());

        return get(id, authentication);
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