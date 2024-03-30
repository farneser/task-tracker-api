package dev.farneser.tasktracker.api.operations.queries.status.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.operations.views.StatusView;
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
public class GetStatusByUserIdQueryHandler implements QueryHandler<GetStatusByUserIdQuery, List<StatusView>> {
    private final ColumnRepository columnRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StatusView> handle(GetStatusByUserIdQuery query) throws NotFoundException {
        List<Status> columns = columnRepository.findByProjectIdOrderByOrderNumber(query.getUserId()).orElse(new ArrayList<>());

        log.debug("Column found: {}", columns);

        List<StatusView> view = columns.stream().map(c -> modelMapper.map(c, StatusView.class)).toList();

        log.debug("Column mapped: {}", Arrays.toString(view.toArray()));

        if (!query.getRetrieveTasks()) {
            view.forEach(c -> c.setTasks(null));
        }

        return view;
    }
}
