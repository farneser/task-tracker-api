package dev.farneser.tasktracker.api.operations.commands.status.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
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
    private final ColumnRepository columnRepository;

    @Override
    public Long handle(CreateStatusCommand command) throws NotFoundException, OperationNotAuthorizedException {

        ProjectMember member = projectMemberRepository.findProjectMemberByProjectIdAndMemberId(command.getProjectId(), command.getUserId()).orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_POST)){
            throw new OperationNotAuthorizedException();
        }

        List<Status> columns = columnRepository.findByProjectIdOrderByOrderNumber(command.getProjectId()).orElse(new ArrayList<>());

        log.debug("Columns found: {}", columns);

        long orderNumber = 1L;

        if (!columns.isEmpty()) {
            orderNumber = columns.get(columns.size() - 1).getOrderNumber() + 1;
        }

        log.debug("Order number: {}", orderNumber);

        Date creationDate = new Date(System.currentTimeMillis());

        Status column = Status
                .builder()
                .statusName(command.getStatusName())
                .isCompleted(command.getIsCompleted())
                .project(member.getProject())
                .orderNumber(orderNumber)
                .creationDate(creationDate)
                .editDate(creationDate)
                .build();

        log.debug("Column created: {}", column);

        columnRepository.save(column);

        log.debug("Column saved: {}", column);

        return column.getId();
    }
}
