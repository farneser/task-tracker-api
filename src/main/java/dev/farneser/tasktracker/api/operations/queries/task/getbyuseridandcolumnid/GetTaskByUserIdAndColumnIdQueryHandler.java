package dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandcolumnid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.queries.task.TaskMapper;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTaskByUserIdAndColumnIdQueryHandler implements QueryHandler<GetTaskByUserIdAndColumnIdQuery, List<TaskView>> {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskView> handle(GetTaskByUserIdAndColumnIdQuery query) throws NotFoundException {
        var tasks = taskRepository.findByUserIdAndColumnIdOrderByOrderNumber(query.getUserId(), query.getColumnId()).orElse(new ArrayList<>());

        log.debug("Tasks found: {}", tasks);

        return taskMapper.mapTaskToTaskView(tasks);
    }
}
