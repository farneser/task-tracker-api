package dev.farneser.tasktracker.api.operations.queries.task.getarchived;

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
public class GetArchivedTaskByUserIdQueryHandler implements QueryHandler<GetArchivedTaskByUserIdQuery, List<TaskView>> {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskView> handle(GetArchivedTaskByUserIdQuery query) throws NotFoundException {
        var tasks = taskRepository
                .findByUserIdOrderByOrderNumber(query.getUserId())
                .orElse(new ArrayList<>())
                .stream()
                .filter(task -> task.getColumn() == null)
                .toList();

        return taskMapper.mapTaskToTaskView(tasks);
    }
}
