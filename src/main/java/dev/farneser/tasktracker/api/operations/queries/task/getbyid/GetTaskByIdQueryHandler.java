package dev.farneser.tasktracker.api.operations.queries.task.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetTaskByIdQueryHandler implements QueryHandler<GetTaskByIdQuery, TaskView> {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Override
    public TaskView handle(GetTaskByIdQuery query) throws NotFoundException {
        var task = taskRepository.findByIdAndUserId(query.getTaskId(), query.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + query.getTaskId() + " not found"));

        var view = modelMapper.map(task, TaskView.class);

        view.getColumn().getTasks().forEach(t -> t.setColumn(null));

        return view;
    }
}
