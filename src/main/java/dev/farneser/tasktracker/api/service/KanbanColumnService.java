package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.kanbancolumn.create.CreateKanbanColumnCommand;
import dev.farneser.tasktracker.api.operations.queries.kanbancolumn.getById.GetKanbanColumnByIdQuery;
import dev.farneser.tasktracker.api.operations.view.KanbanColumnView;
import dev.farneser.tasktracker.api.web.dto.KanbanColumnDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KanbanColumnService extends BaseService {
    @Autowired
    public KanbanColumnService(Mediator mediator, ModelMapper modelMapper) {
        super(mediator, modelMapper);
    }

    public KanbanColumnView create(KanbanColumnDto dto, Authentication authentication) throws NotFoundException {

        var user = this.getUser(authentication);

        var command = modelMapper.map(dto, CreateKanbanColumnCommand.class);

        command.setUserId(user.getId());

        var columnId = mediator.send(command);

        return mediator.send(new GetKanbanColumnByIdQuery(user.getId(), columnId));
    }
}