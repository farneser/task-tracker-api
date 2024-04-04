package dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandstatusid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.Task;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.queries.task.TaskMapper;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTaskByUserIdAndStatusIdQueryHandler implements QueryHandler<GetTaskByUserIdAndStatusIdQuery, List<TaskLookupView>> {
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final StatusRepository statusRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskLookupView> handle(GetTaskByUserIdAndStatusIdQuery query) throws NotFoundException, OperationNotAuthorizedException {

        List<Task> tasks = taskRepository
                .findByStatusIdOrderByOrderNumber(query.getStatusId())
                .orElse(new ArrayList<>());

        Status status = statusRepository.findById(query.getStatusId()).orElseThrow(() -> new NotFoundException(""));

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(status.getProject().getId(), query.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.USER_GET)) {
            throw new OperationNotAuthorizedException();
        }

        log.debug("Tasks found: {}", tasks);

        return taskMapper.mapTaskToTaskLookupView(tasks);
    }
}
