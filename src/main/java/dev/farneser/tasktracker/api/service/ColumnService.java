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
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.web.dto.column.CreateColumnDto;
import dev.farneser.tasktracker.api.web.dto.column.PatchColumnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public ColumnView create(CreateColumnDto dto, Authentication authentication) throws NotFoundException {
        log.debug("Creating column {}", dto.getColumnName());

        var user = userService.getUser(authentication);

        log.debug("Creating column {}", dto.getColumnName());

        var command = modelMapper.map(dto, CreateColumnCommand.class);

        log.debug("Creating column {}", dto.getColumnName());

        command.setUserId(user.getId());

        log.debug("Creating column {}", dto.getColumnName());

        var columnId = mediator.send(command);

        log.debug("Created column {}", dto.getColumnName());

        return get(columnId, authentication);
    }

    public List<ColumnView> get(Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Getting columns for user {}", user.getEmail());

        return mediator.send(new GetColumnByUserIdQuery(user.getId()));
    }

    public ColumnView get(Long id, Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Getting column {} for user {}", id, user.getEmail());

        return mediator.send(new GetColumnByIdQuery(user.getId(), id));
    }

    public void delete(Long id, Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Deleting column {} for user {}", id, user.getEmail());

        mediator.send(new DeleteColumnCommand(user.getId(), id));
    }

    public ColumnView patch(Long id, PatchColumnDto patchColumnDto, Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Patching column {} for user {}", id, user.getEmail());

        var command = modelMapper.map(patchColumnDto, PatchColumnCommand.class);

        log.debug("Patching column {} for user {}", id, user.getEmail());

        command.setColumnId(id);
        command.setUserId(user.getId());

        log.debug("Patching column {} for user {}", id, user.getEmail());

        mediator.send(command);

        log.debug("Patched column {} for user {}", id, user.getEmail());

        return get(id, authentication);
    }

    public List<TaskView> getTasks(Long id, Authentication authentication) throws NotFoundException {
        var user = userService.getUser(authentication);

        log.debug("Getting tasks for column {} for user {}", id, user.getEmail());

        return mediator.send(new GetTaskByUserIdAndColumnIdQuery(user.getId(), id));
    }
}