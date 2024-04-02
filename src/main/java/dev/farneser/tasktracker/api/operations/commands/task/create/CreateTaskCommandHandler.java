package dev.farneser.tasktracker.api.operations.commands.task.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.Task;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTaskCommandHandler implements CommandHandler<CreateTaskCommand, Long> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Long handle(CreateTaskCommand command) throws NotFoundException, OperationNotAuthorizedException {
        Status status = statusRepository
                .findById(command.getStatusId())
                .orElseThrow(() -> new NotFoundException("Column with id " + command.getStatusId() + " of user id " + command.getUserId() + " not found"));

        log.debug("Column found: {}", status);

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(status.getProject().getId(), command.getUserId())
                .orElseThrow(()->new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.USER_POST)){
            throw new OperationNotAuthorizedException();
        }

        long orderNumber = 1L;

        if (status.getTasks() != null) {

            status.getTasks().sort(Comparator.comparing(Task::getOrderNumber));

            if (!status.getTasks().isEmpty()) {
                log.debug("Tasks found: {}", status.getTasks());

                orderNumber = status.getTasks().get(status.getTasks().size() - 1).getOrderNumber() + 1;
            }
        }

        Date creationDate = new Date(System.currentTimeMillis());

        log.debug("Order number: {}", orderNumber);

        User assignedFor = null;

        if (command.getAssignedFor() != null){

            ProjectMember assignedMember = projectMemberRepository
                    .findProjectMemberByProjectIdAndMemberId(status.getProject().getId(), command.getAssignedFor())
                    .orElseThrow(()->new NotFoundException(""));

            assignedFor = assignedMember.getMember();
        }

        Task task = Task.builder()
                .taskName(command.getTaskName())
                .description(command.getDescription())
                .orderNumber(orderNumber)
                .status(status)
                .assignedFor(assignedFor)
                .creationDate(creationDate)
                .editDate(creationDate)
                .build();

        log.debug("Task created: {}", task);

        status.getTasks().add(task);

        log.debug("Task added to status: {}", status);

        statusRepository.save(status);

        log.debug("Column saved: {}", status);

        return task.getId();
    }
}
