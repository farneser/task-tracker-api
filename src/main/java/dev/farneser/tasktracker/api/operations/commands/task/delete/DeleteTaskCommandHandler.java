package dev.farneser.tasktracker.api.operations.commands.task.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Task;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
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
public class DeleteTaskCommandHandler implements CommandHandler<DeleteTaskCommand, Void> {
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Void handle(DeleteTaskCommand command) throws NotFoundException, OperationNotAuthorizedException {

        Task task = taskRepository
                .findById(command.getTaskId())
                .orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        ProjectMember member = projectMemberRepository
                .findByProjectIdAndMemberId(task.getStatus().getProject().getId(), command.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.USER_DELETE)) {
            throw new OperationNotAuthorizedException();
        }

        log.debug("Task found: {}", task);

        List<Task> tasks = taskRepository
                .findByStatusIdOrderByOrderNumber(task.getStatus().getId())
                .orElse(new ArrayList<>());

        log.debug("Tasks found: {}", tasks);

        List<Task> tasksToUpdate = tasks.stream().filter(t -> t.getOrderNumber() > task.getOrderNumber()).toList();

        log.debug("Tasks to update: {}", tasksToUpdate);

        tasksToUpdate.forEach(t -> t.setOrderNumber(t.getOrderNumber() - 1));

        log.debug("Tasks to update after: {}", tasksToUpdate);

        taskRepository.saveAll(tasksToUpdate);

        log.debug("Tasks updated");

        taskRepository.delete(task);

        log.debug("Task deleted: {}", command.getTaskId());

        return null;
    }
}
