package dev.farneser.tasktracker.api.operations.queries.column.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetColumnByUserIdQueryHandler implements QueryHandler<GetColumnByUserIdQuery, List<ColumnView>> {
    private final ColumnRepository columnRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ColumnView> handle(GetColumnByUserIdQuery query) throws NotFoundException {
        var columns = columnRepository.findByUserIdOrderByOrderNumber(query.getUserId()).orElse(new ArrayList<>());

        log.debug("Column found: {}", columns);

        var view = columns.stream().map(c -> modelMapper.map(c, ColumnView.class)).toList();

        log.debug("Column mapped: {}", Arrays.toString(view.toArray()));

        if (!query.getRetrieveTasks()) {
            view.forEach(c -> c.setTasks(null));
        }

        return view;
    }
}
