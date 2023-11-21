package dev.farneser.tasktracker.api.operations.queries.column.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetColumnByIdQueryHandler implements QueryHandler<GetColumnByIdQuery, ColumnView> {
    private final ColumnRepository columnRepository;
    private final ModelMapper modelMapper;

    @Override
    public ColumnView handle(GetColumnByIdQuery query) throws NotFoundException {
        var column = columnRepository.findByIdAndUserId(query.getColumnId(), query.getUserId()).orElseThrow(() -> new NotFoundException("Kanban column with id " + query.getColumnId() + " not found"));

        return modelMapper.map(column, ColumnView.class);
    }
}
