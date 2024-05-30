package dev.farneser.tasktracker.api.operations.queries.task.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
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
public class GetTaskByUserIdQueryHandler implements QueryHandler<GetTaskByUserIdQuery, List<TaskLookupView>> {
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskLookupView> handle(GetTaskByUserIdQuery query) throws NotFoundException {
        List<ProjectMember> projectMembers = projectMemberRepository
                .findByMemberId(query.getUserId())
                .orElse(new ArrayList<>());

        List<Task> result = new ArrayList<>();

        projectMembers.forEach(m -> result.addAll(
                taskRepository
                        .findAllByProjectId(m.getProject().getId())
                        .orElse(new ArrayList<>()))
        );

        return taskMapper.mapTaskToTaskLookupView(result);
    }
}
