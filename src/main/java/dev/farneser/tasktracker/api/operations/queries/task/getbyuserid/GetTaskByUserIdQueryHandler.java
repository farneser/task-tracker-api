package dev.farneser.tasktracker.api.operations.queries.task.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.operations.queries.task.TaskMapper;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTaskByUserIdQueryHandler implements QueryHandler<GetTaskByUserIdQuery, List<TaskLookupView>> {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskLookupView> handle(GetTaskByUserIdQuery query) throws NotFoundException {
        List<KanbanTask> tasks = taskRepository.findByUserIdOrderByOrderNumber(query.getUserId()).orElse(new ArrayList<>());

        log.debug("Tasks found: {}", tasks);

        return taskMapper.mapTaskToTaskLookupView(tasks);
    }
}
