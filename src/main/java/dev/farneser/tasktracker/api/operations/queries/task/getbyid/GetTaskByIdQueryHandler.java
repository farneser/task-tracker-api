package dev.farneser.tasktracker.api.operations.queries.task.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTaskByIdQueryHandler implements QueryHandler<GetTaskByIdQuery, TaskView> {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Override
    public TaskView handle(GetTaskByIdQuery query) throws NotFoundException {
        var task = taskRepository.findByIdAndUserId(query.getTaskId(), query.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + query.getTaskId() + " not found"));

        log.debug("Task found: {}", task);

        var view = modelMapper.map(task, TaskView.class);

        log.debug("Task mapped: {}", view);

        if (view.getColumn() != null && view.getColumn().getTasks() != null) {
            log.debug("Tasks found: {}", view.getColumn().getTasks());

            view.getColumn().getTasks().forEach(t -> t.setColumn(null));
        }

        return view;
    }
}
