package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.task.create.CreateTaskCommand;
import dev.farneser.tasktracker.api.operations.commands.task.delete.DeleteTaskCommand;
import dev.farneser.tasktracker.api.operations.commands.task.patch.PatchTaskCommand;
import dev.farneser.tasktracker.api.operations.queries.task.getbyid.GetTaskByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuserid.GetTaskByUserIdQuery;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.web.dto.task.CreateTaskDto;
import dev.farneser.tasktracker.api.web.dto.task.PatchTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService extends BaseService {
    @Autowired
    public TaskService(Mediator mediator, ModelMapper modelMapper) {
        super(mediator, modelMapper);
    }

    public TaskView create(CreateTaskDto dto, Authentication authentication) throws NotFoundException {

        var user = this.getUser(authentication);

        var command = modelMapper.map(dto, CreateTaskCommand.class);

        command.setUserId(user.getId());

        var columnId = mediator.send(command);

        return get(columnId, authentication);
    }

    public List<TaskView> get(Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        return mediator.send(new GetTaskByUserIdQuery(user.getId()));
    }

    public TaskView get(Long id, Authentication authentication) throws NotFoundException {
        var user = this.getUser(authentication);

        return mediator.send(new GetTaskByIdQuery(user.getId(), id));
    }

    public void delete(Long id, Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        mediator.send(new DeleteTaskCommand(user.getId(), id));
    }

    public TaskView patch(Long id, PatchTaskDto patchTaskDto, Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        var command = modelMapper.map(patchTaskDto, PatchTaskCommand.class);

        command.setTaskId(id);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(id, authentication);
    }
}