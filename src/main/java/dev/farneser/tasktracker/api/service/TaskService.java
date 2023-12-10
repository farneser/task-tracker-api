package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.task.archive.ArchiveTasksCommand;
import dev.farneser.tasktracker.api.operations.commands.task.create.CreateTaskCommand;
import dev.farneser.tasktracker.api.operations.commands.task.delete.DeleteTaskCommand;
import dev.farneser.tasktracker.api.operations.commands.task.patch.PatchTaskCommand;
import dev.farneser.tasktracker.api.operations.queries.task.getarchived.GetArchivedTaskByUserIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyid.GetTaskByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuserid.GetTaskByUserIdQuery;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.operations.views.task.TaskView;
import dev.farneser.tasktracker.api.web.dto.task.CreateTaskDto;
import dev.farneser.tasktracker.api.web.dto.task.PatchTaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public TaskView create(CreateTaskDto dto, Authentication authentication) throws NotFoundException {
        log.debug("Creating task {}", dto.getTaskName());

        var user = userService.getUser(authentication);

        log.debug("Creating task {}", dto.getTaskName());

        var command = modelMapper.map(dto, CreateTaskCommand.class);

        log.debug("Creating task {}", dto.getTaskName());

        command.setUserId(user.getId());

        log.debug("Creating task {}", dto.getTaskName());

        var columnId = mediator.send(command);

        log.debug("Created task {}", dto.getTaskName());

        return get(columnId, authentication);
    }

    public List<TaskLookupView> get(Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Getting tasks for user {}", user.getEmail());

        return mediator.send(new GetTaskByUserIdQuery(user.getId()));
    }

    public TaskView get(Long id, Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Getting task {} for user {}", id, user.getEmail());

        return mediator.send(new GetTaskByIdQuery(user.getId(), id));
    }

    public void delete(Long id, Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Deleting task {} for user {}", id, user.getEmail());

        mediator.send(new DeleteTaskCommand(user.getId(), id));
    }

    public TaskView patch(Long id, PatchTaskDto patchTaskDto, Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Patching task {} for user {}", id, user.getEmail());

        var command = modelMapper.map(patchTaskDto, PatchTaskCommand.class);

        command.setTaskId(id);
        command.setUserId(user.getId());

        log.debug("Patching task {} for user {}", id, user.getEmail());

        mediator.send(command);

        log.debug("Patched task {} for user {}", id, user.getEmail());

        return get(id, authentication);
    }

    public List<TaskLookupView> getArchived(Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Getting archived tasks for user {}", user.getEmail());

        return mediator.send(new GetArchivedTaskByUserIdQuery(user.getId()));
    }

    public void archiveTasks(Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Archiving tasks for user {}", user.getEmail());

        mediator.send(new ArchiveTasksCommand(user.getId()));
    }
}