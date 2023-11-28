package dev.farneser.tasktracker.api.operations.queries.task.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTaskByUserIdQueryHandler implements QueryHandler<GetTaskByUserIdQuery, List<TaskView>> {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TaskView> handle(GetTaskByUserIdQuery query) throws NotFoundException {
        var tasks = taskRepository.findByUserIdOrderByOrderNumber(query.getUserId()).orElse(new ArrayList<>());

        var view = tasks.stream().map(task -> modelMapper.map(task, TaskView.class)).toList();

        view.forEach(task -> task.getColumn().getTasks().forEach(t -> t.setColumn(null)));

        return view;
    }
}
