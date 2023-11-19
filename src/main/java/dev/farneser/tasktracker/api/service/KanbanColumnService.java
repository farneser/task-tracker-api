package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.kanbancolumn.create.CreateKanbanColumnCommand;
import dev.farneser.tasktracker.api.operations.commands.kanbancolumn.delete.DeleteKanbanColumnCommand;
import dev.farneser.tasktracker.api.operations.commands.kanbancolumn.patch.PatchKanbanColumnCommand;
import dev.farneser.tasktracker.api.operations.queries.kanbancolumn.getbyid.GetKanbanColumnByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.kanbancolumn.getbyuserid.GetKanbanColumnByUserIdQuery;
import dev.farneser.tasktracker.api.operations.views.KanbanColumnView;
import dev.farneser.tasktracker.api.web.dto.kanbancolumn.CreateKanbanColumnDto;
import dev.farneser.tasktracker.api.web.dto.kanbancolumn.PatchKanbanColumnDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KanbanColumnService extends BaseService {
    @Autowired
    public KanbanColumnService(Mediator mediator, ModelMapper modelMapper) {
        super(mediator, modelMapper);
    }

    public KanbanColumnView create(CreateKanbanColumnDto dto, Authentication authentication) throws NotFoundException {

        var user = this.getUser(authentication);

        var command = modelMapper.map(dto, CreateKanbanColumnCommand.class);

        command.setUserId(user.getId());

        var columnId = mediator.send(command);

        return get(columnId, authentication);
    }

    public List<KanbanColumnView> get(Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        return mediator.send(new GetKanbanColumnByUserIdQuery(user.getId()));
    }

    public KanbanColumnView get(Long id, Authentication authentication) throws NotFoundException {
        var user = this.getUser(authentication);

        return mediator.send(new GetKanbanColumnByIdQuery(user.getId(), id));
    }

    public void delete(Long id, Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        mediator.send(new DeleteKanbanColumnCommand(user.getId(), id));
    }

    public KanbanColumnView patch(Long id, PatchKanbanColumnDto patchKanbanColumnDto, Authentication authentication) throws NotFoundException {
        var user = getUser(authentication);

        var command = modelMapper.map(patchKanbanColumnDto, PatchKanbanColumnCommand.class);

        command.setColumnId(id);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(id, authentication);
    }
}