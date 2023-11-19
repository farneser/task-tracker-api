package dev.farneser.tasktracker.api.operations.queries.kanbancolumn.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.KanbanColumnView;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetKanbanColumnByIdQueryHandler implements QueryHandler<GetKanbanColumnByIdQuery, KanbanColumnView> {
    private final KanbanColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public KanbanColumnView handle(GetKanbanColumnByIdQuery query) throws NotFoundException {
        var column = columnRepository.findByIdAndUserId(query.getColumnId(), query.getUserId()).orElseThrow(() -> new NotFoundException("Kanban column with id " + query.getColumnId() + " not found"));

        return modelMapper.map(column, KanbanColumnView.class);
    }
}
