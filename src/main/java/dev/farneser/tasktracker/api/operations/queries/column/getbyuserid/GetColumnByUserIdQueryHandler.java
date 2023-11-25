package dev.farneser.tasktracker.api.operations.queries.column.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetColumnByUserIdQueryHandler implements QueryHandler<GetColumnByUserIdQuery, List<ColumnView>> {
    private final ColumnRepository columnRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ColumnView> handle(GetColumnByUserIdQuery query) throws NotFoundException {
        var column = columnRepository.findByUserIdOrderByOrderNumber(query.getUserId()).orElse(new ArrayList<>());

        return column.stream().map(c -> modelMapper.map(c, ColumnView.class)).collect(Collectors.toList());
    }
}
