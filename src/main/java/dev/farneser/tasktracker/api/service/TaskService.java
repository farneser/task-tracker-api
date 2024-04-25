package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.task.archive.ArchiveTasksCommand;
import dev.farneser.tasktracker.api.operations.commands.task.create.CreateTaskCommand;
import dev.farneser.tasktracker.api.operations.commands.task.delete.DeleteTaskCommand;
import dev.farneser.tasktracker.api.operations.commands.task.patch.PatchTaskCommand;
import dev.farneser.tasktracker.api.operations.queries.task.getarchivedbyuserid.GetArchivedTaskByUserIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyid.GetTaskByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuserid.GetTaskByUserIdQuery;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.operations.views.task.TaskView;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.dto.task.CreateTaskDto;
import dev.farneser.tasktracker.api.dto.task.PatchTaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The `TaskService` class provides functionality related to task management.
 * It allows creating, retrieving, updating, and deleting tasks for a user.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserService userService;

    /**
     * Creates a new task for the authenticated user.
     *
     * @param dto            The data to create the task.
     * @param authentication The authentication object representing the current user.
     * @return The created task view.
     * @throws NotFoundException               If the user is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public TaskView create(CreateTaskDto dto, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.debug("Creating task {}", dto.getTaskName());

        UserView user = userService.getUser(authentication);

        CreateTaskCommand command = modelMapper.map(dto, CreateTaskCommand.class);

        command.setUserId(user.getId());

        Long taskId = mediator.send(command);

        log.debug("Created task {}", dto.getTaskName());

        return get(taskId, authentication);
    }

    /**
     * Retrieves all tasks for the authenticated user.
     *
     * @param authentication The authentication object representing the current user.
     * @return The list of task lookup views.
     * @throws NotFoundException               If the user is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public List<TaskLookupView> get(UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting tasks for user {}", user.getEmail());

        return mediator.send(new GetTaskByUserIdQuery(user.getId()));
    }

    /**
     * Retrieves a specific task for the authenticated user.
     *
     * @param id             The ID of the task to retrieve.
     * @param authentication The authentication object representing the current user.
     * @return The task view for the specified task ID.
     * @throws NotFoundException               If the user is not found or the task with the given ID is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public TaskView get(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting task {} for user {}", id, user.getEmail());

        return mediator.send(new GetTaskByIdQuery(user.getId(), id));
    }

    /**
     * Deletes a specific task for the authenticated user.
     *
     * @param id             The ID of the task to delete.
     * @param authentication The authentication object representing the current user.
     * @throws NotFoundException               If the user is not found or the task with the given ID is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public void delete(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        log.debug("Deleting task {} for user {}", id, user.getEmail());

        mediator.send(new DeleteTaskCommand(user.getId(), id));
    }

    /**
     * Updates a specific task for the authenticated user.
     *
     * @param id             The ID of the task to update.
     * @param patchTaskDto   The data to patch the task.
     * @param authentication The authentication object representing the current user.
     * @return The updated task view for the specified task ID.
     * @throws NotFoundException               If the user is not found or the task with the given ID is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public TaskView patch(Long id, PatchTaskDto patchTaskDto, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        log.debug("Patching task {} for user {}", id, user.getEmail());

        PatchTaskCommand command = modelMapper.map(patchTaskDto, PatchTaskCommand.class);

        command.setTaskId(id);
        command.setUserId(user.getId());

        mediator.send(command);

        log.debug("Patched task {} for user {}", id, user.getEmail());

        return get(id, authentication);
    }

    /**
     * Retrieves all archived tasks for the authenticated user.
     *
     * @param authentication The authentication object representing the current user.
     * @return The list of archived task lookup views.
     * @throws NotFoundException               If the user is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public List<TaskLookupView> getArchived(UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        log.debug("Getting archived tasks for user {}", user.getEmail());

        return mediator.send(new GetArchivedTaskByUserIdQuery(user.getId()));
    }

    /**
     * Archives all tasks for the authenticated user.
     *
     * @param authentication The authentication object representing the current user.
     * @throws NotFoundException               If the user is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public void archiveTasks(UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        log.debug("Archiving tasks for user {}", user.getEmail());

        mediator.send(new ArchiveTasksCommand(user.getId()));
    }
}