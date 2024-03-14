package dev.farneser.tasktracker.api.operations.queries.task.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.operations.views.task.TaskView;
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
        KanbanTask task = taskRepository.findByIdAndUserId(query.getTaskId(), query.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + query.getTaskId() + " not found"));

        log.debug("Task found: {}", task);

        TaskView view = modelMapper.map(task, TaskView.class);

        if (view.getColumn() != null) {
            view.getColumn().setTasks(null);
        }

        log.debug("Task mapped: {}", view);

        return view;
    }
}
