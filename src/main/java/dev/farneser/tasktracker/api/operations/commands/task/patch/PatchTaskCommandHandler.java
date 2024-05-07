package dev.farneser.tasktracker.api.operations.commands.task.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.Task;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.views.order.OrderUtility;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchTaskCommandHandler implements CommandHandler<PatchTaskCommand, Void> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;

    private static void patchOrder(long orderNumber, Task task) {
        if (task.getStatus() != null) {
            long oldOrder = task.getOrderNumber() == null ? -1L : task.getOrderNumber();

            log.debug("Old order: {}", oldOrder);

            task.setOrderNumber(orderNumber);

            List<Task> tasksToChange = task.getStatus().getTasks().stream().filter(c -> c.getOrderNumber() >= Math.min(oldOrder, orderNumber) && c.getOrderNumber() <= Math.max(oldOrder, orderNumber)).toList();

            log.debug("Tasks to change: {}", tasksToChange);

            OrderUtility.patchOrder(task.getId(), orderNumber, oldOrder, tasksToChange);
        } else {
            log.debug("Status is null");

            task.setOrderNumber(null);
        }
    }

    @Override
    public Void handle(PatchTaskCommand command) throws NotFoundException, OperationNotAuthorizedException {
        Task task = taskRepository.findById(command.getTaskId()).orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        log.debug("Task found: {}", task);

        ProjectMember member = projectMemberRepository
                .findByProjectIdAndMemberId(task.getProject().getId(), command.getUserId())
                .orElseThrow(() -> new ProjectMemberNotFoundException(command.getUserId()));

        if (!member.getRole().hasPermission(ProjectPermission.USER_PATCH)) {
            throw new OperationNotAuthorizedException();
        }

        if (command.getStatusId() != null) {
            log.debug("Status id: {}", command.getStatusId());

            if (command.getStatusId() == -1) {
                log.debug("Status set to null");
                task.setStatus(null);
            } else {
                log.debug("Status set to {}", command.getStatusId());

                Status status = statusRepository
                        .findByIdAndProjectId(command.getStatusId(), member.getProject().getId())
                        .orElseThrow(() -> new NotFoundException("Status with id " + command.getStatusId() + " not found"));

                task.setStatus(status);
            }
        }

        if (command.getOrderNumber() != null) {
            log.debug("Order number changed from {} to {}", task.getOrderNumber(), command.getOrderNumber());

            patchOrder(command.getOrderNumber(), task);
        }

        if (command.getTaskName() != null) {
            log.debug("Task name changed from {} to {}", task.getTaskName(), command.getTaskName());

            task.setTaskName(command.getTaskName());
        }

        if (command.getDescription() != null) {
            log.debug("Description changed from {} to {}", task.getDescription(), command.getDescription());

            task.setDescription(command.getDescription());
        }

        if (command.getAssignedFor() != null) {
            log.debug("Description changed from {} to {}", task.getDescription(), command.getDescription());

            ProjectMember assignedFor = projectMemberRepository
                    .findByProjectIdAndMemberId(member.getProject().getId(), command.getAssignedFor())
                    .orElseThrow(() -> new UserNotFoundException(command.getAssignedFor()));

            task.setAssignedFor(assignedFor.getMember());
        }

        task.setEditDate(new Date(System.currentTimeMillis()));

        log.debug("Task updated: {}", task);

        taskRepository.save(task);

        log.debug("Task saved: {}", task);

        return null;
    }
}
