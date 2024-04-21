package dev.farneser.tasktracker.api.operations.commands.status.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateStatusCommandHandler implements CommandHandler<CreateStatusCommand, Long> {
    private final ProjectMemberRepository projectMemberRepository;
    private final StatusRepository statusRepository;

    @Override
    public Long handle(CreateStatusCommand command) throws NotFoundException, OperationNotAuthorizedException {

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(command.getProjectId(), command.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_POST)) {
            throw new OperationNotAuthorizedException();
        }

        List<Status> statuses = statusRepository
                .findByProjectIdOrderByOrderNumber(command.getProjectId())
                .orElse(new ArrayList<>());

        log.debug("Statuses found: {}", statuses);

        long orderNumber = 1L;

        if (!statuses.isEmpty()) {
            orderNumber = statuses.get(statuses.size() - 1).getOrderNumber() + 1;
        }

        log.debug("Order number: {}", orderNumber);

        Date creationDate = new Date(System.currentTimeMillis());

        Status status = Status
                .builder()
                .statusName(command.getStatusName())
                .statusColor(command.getStatusColor())
                .isCompleted(command.getIsCompleted())
                .project(member.getProject())
                .orderNumber(orderNumber)
                .creationDate(creationDate)
                .editDate(creationDate)
                .build();

        log.debug("Status created: {}", status);

        statusRepository.save(status);

        log.debug("Status saved: {}", status);

        return status.getId();
    }
}
