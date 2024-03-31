package dev.farneser.tasktracker.api.operations.queries.status.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetStatusByIdQueryHandler implements QueryHandler<GetStatusByIdQuery, StatusView> {
    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;

    @Override
    public StatusView handle(GetStatusByIdQuery query) throws NotFoundException {
        Status column = statusRepository.findByIdAndProjectId(query.getStatusId(), query.getUserId()).orElseThrow(() -> new NotFoundException("Kanban column with id " + query.getStatusId() + " not found"));

        log.debug("Column found: {}", column);

        StatusView view = modelMapper.map(column, StatusView.class);

        log.debug("Column mapped: {}", view);

        return view;
    }
}
