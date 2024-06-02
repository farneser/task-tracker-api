package dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandprojectid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
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
public class GetTaskByUserIdAndProjectIdQueryHandler implements QueryHandler<GetTaskByUserIdAndProjectIdQuery, List<TaskLookupView>> {
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskLookupView> handle(GetTaskByUserIdAndProjectIdQuery query) throws NotFoundException, OperationNotAuthorizedException {
        ProjectMember member = projectMemberRepository
                .findByProjectIdAndMemberId(query.getProjectId(), query.getUserId())
                .orElseThrow(() -> new ProjectMemberNotFoundException(query.getUserId()));

        if (!member.getRole().hasPermission(ProjectPermission.USER_GET)) {
            throw new OperationNotAuthorizedException("");
        }

        return taskMapper.mapTaskToTaskLookupView(taskRepository.findAllByProjectId(query.getProjectId()).orElse(new ArrayList<>()));
    }
}
