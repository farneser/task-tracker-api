package dev.farneser.tasktracker.api.operations.queries.task.getarchivedbyprojectid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.Task;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.operations.queries.task.TaskMapper;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetArchivedTaskByProjectIdQueryHandler implements QueryHandler<GetArchivedTaskByProjectIdQuery, List<TaskLookupView>> {
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskLookupView> handle(GetArchivedTaskByProjectIdQuery query) throws NotFoundException {
        ProjectMember projectMember = projectMemberRepository
                .findByProjectIdAndMemberId(query.getProjectId(), query.getUserId())
                .orElseThrow(() -> new ProjectMemberNotFoundException(query.getUserId()));

        List<Task> projectTasks = taskRepository
                .findByProjectIdAndStatusLessThanOneOrNull(projectMember.getProject().getId())
                .orElse(new ArrayList<>());

        return taskMapper.mapTaskToTaskLookupView(projectTasks
                .stream()
                .filter(t -> t.getStatus() == null)
                .toList()
        );
    }
}
