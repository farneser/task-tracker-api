package dev.farneser.tasktracker.api.operations.queries.task.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.Task;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.views.task.TaskView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTaskByIdQueryHandler implements QueryHandler<GetTaskByIdQuery, TaskView> {
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper modelMapper;

    @Override
    public TaskView handle(GetTaskByIdQuery query) throws NotFoundException, OperationNotAuthorizedException {
        Task task = taskRepository
                .findById(query.getTaskId())
                .orElseThrow(() -> new NotFoundException("Task with id " + query.getTaskId() + " not found"));

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(task.getStatus().getProject().getId(), query.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.USER_GET)) {
            throw new OperationNotAuthorizedException();
        }

        log.debug("Task found: {}", task);

        TaskView view = modelMapper.map(task, TaskView.class);

        if (view.getStatus() != null) {
            view.getStatus().setTasks(null);
        }

        log.debug("Task mapped: {}", view);

        return view;
    }
}
