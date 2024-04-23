package dev.farneser.tasktracker.api.operations.queries.task.getarchivedbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.Task;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.operations.queries.task.TaskMapper;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetArchivedTaskByUserIdQueryHandler implements QueryHandler<GetArchivedTaskByUserIdQuery, List<TaskLookupView>> {
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskLookupView> handle(GetArchivedTaskByUserIdQuery query) throws NotFoundException {

        List<ProjectMember> projectMembers = projectMemberRepository
                .findByMemberId(query.getUserId())
                .orElse(new ArrayList<>());

        List<Task> tasks = new ArrayList<>();

        projectMembers.forEach(pm -> pm.getProject().getStatuses()
                .forEach(s -> tasks.addAll(s
                        .getTasks()
                        .stream()
                        .filter(task -> task.getStatus() == null)
                        .toList())
                )
        );

        return taskMapper.mapTaskToTaskLookupView(tasks);
    }
}
