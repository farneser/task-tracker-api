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
import dev.farneser.tasktracker.api.operations.views.order.OrderIdentifier;
import dev.farneser.tasktracker.api.operations.views.order.OrderUtility;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchTaskCommandHandler implements CommandHandler<PatchTaskCommand, Void> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;

    private void patchOrder(long orderNumber, Task task) {
        long oldOrder = task.getOrderNumber() == null ? -1L : task.getOrderNumber();
        log.debug("Old order: {}", oldOrder);

        if (orderNumber <= 0) {
            log.warn("Invalid order number: {}. Setting to 1.", orderNumber);
            orderNumber = 1;
        }

        List<Task> relevantTasks;
        if (task.getStatus() != null) {
            relevantTasks = task.getStatus().getTasks();
        } else {
            log.debug("Status is null");

            relevantTasks = taskRepository.findByProjectIdAndStatusLessThanOneOrNull(task.getProject().getId()).orElse(new ArrayList<>());

            if (relevantTasks.isEmpty()) {
                log.warn("No archived tasks found for project ID: {}", task.getProject().getId());

                return;
            }
        }

        if (orderNumber > relevantTasks.size()) {
            log.warn("Order number exceeds the number of tasks: {}. Setting to maximum value.", orderNumber);

            orderNumber = relevantTasks.size();
        }

        OrderUtility.patchOrder(task, orderNumber, relevantTasks);

        taskRepository.saveAll(relevantTasks);
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

        patchTask(command, task, member.getProject().getId());

        task.setEditDate(new Date(System.currentTimeMillis()));

        log.debug("Task updated: {}", task);

        taskRepository.save(task);

        log.debug("Task saved: {}", task);

        return null;
    }

    private void patchTask(PatchTaskCommand command, Task task, Long projectId) throws NotFoundException {
        if (command.getStatusId() != null) {
            log.debug("Status id: {}", command.getStatusId());

            if (command.getStatusId() == -1L) {
                log.debug("Status set to null");
                task.setStatus(null);
            } else {
                log.debug("Status set to {}", command.getStatusId());

                Status status = statusRepository
                        .findByIdAndProjectId(command.getStatusId(), projectId)
                        .orElseThrow(() -> new NotFoundException("Status with id " + command.getStatusId() + " not found"));

                OrderUtility.removeSpace(task
                        .getStatus().getTasks().stream()
                        .filter(t -> !t.getId().equals(task.getId()))
                        .sorted(Comparator.comparing(OrderIdentifier::getOrderNumber)).toList()
                );

                task.setStatus(status);

                taskRepository.saveAll(status.getTasks());
                statusRepository.save(status);
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
            log.debug("Assigned for changed from {} to {}", task.getAssignedFor(), command.getAssignedFor());

            if (command.getAssignedFor() == -1L) {
                task.setAssignedFor(null);
            } else {
                ProjectMember assignedFor = projectMemberRepository
                        .findByProjectIdAndMemberId(projectId, command.getAssignedFor())
                        .orElseThrow(() -> new UserNotFoundException(command.getAssignedFor()));

                task.setAssignedFor(assignedFor.getMember());
            }
        }
    }
}
