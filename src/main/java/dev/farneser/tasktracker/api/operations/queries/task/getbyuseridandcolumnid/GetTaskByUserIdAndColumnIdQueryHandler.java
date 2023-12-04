package dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandcolumnid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import dev.farneser.tasktracker.api.operations.queries.task.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTaskByUserIdAndColumnIdQueryHandler implements QueryHandler<GetTaskByUserIdAndColumnIdQuery, List<TaskView>> {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskView> handle(GetTaskByUserIdAndColumnIdQuery query) throws NotFoundException {
        var tasks = taskRepository.findByUserIdAndColumnIdOrderByOrderNumber(query.getUserId(), query.getColumnId()).orElse(new ArrayList<>());

        return taskMapper.mapTaskToTaskView(tasks);
    }
}
