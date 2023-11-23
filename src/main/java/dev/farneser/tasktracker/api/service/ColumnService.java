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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ColumnService extends BaseService {
    @Autowired
    public ColumnService(Mediator mediator, ModelMapper modelMapper) {
        super(mediator, modelMapper);
    }

    public ColumnView create(CreateColumnDto dto, Authentication authentication) throws NotFoundException {

        var user = this.getUser(authentication);

        var command = modelMapper.map(dto, CreateColumnCommand.class);

        command.setUserId(user.getId());

        var columnId = mediator.send(command);

        return get(columnId, authentication);
    }

    public List<ColumnView> get(Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        return mediator.send(new GetColumnByUserIdQuery(user.getId()));
    }

    public ColumnView get(Long id, Authentication authentication) throws NotFoundException {
        var user = this.getUser(authentication);

        return mediator.send(new GetColumnByIdQuery(user.getId(), id));
    }

    public void delete(Long id, Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        mediator.send(new DeleteColumnCommand(user.getId(), id));
    }

    public ColumnView patch(Long id, PatchColumnDto patchColumnDto, Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        var command = modelMapper.map(patchColumnDto, PatchColumnCommand.class);

        command.setColumnId(id);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(id, authentication);
    }

    public List<TaskView> getTasks(Long id, Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        return mediator.send(new GetTaskByUserIdAndColumnIdQuery(user.getId(), id));
    }
}