package dev.farneser.tasktracker.api.operations.commands.status.delete;

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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteStatusCommandHandler implements CommandHandler<DeleteStatusCommand, Void> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Void handle(DeleteStatusCommand command) throws NotFoundException, OperationNotAuthorizedException {

        Status column = statusRepository
                .findById(command.getStatusId())
                .orElseThrow(() -> new NotFoundException("Column with id " + command.getStatusId() + " not found"));

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(column.getProject().getId(), command.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_DELETE)) {
            throw new OperationNotAuthorizedException();
        }

        log.debug("Column found: {}", column);

        List<Status> columns = statusRepository.findByProjectIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        log.debug("Columns found: {}", columns);

        List<Status> columnsToUpdate = columns.stream().filter(c -> c.getOrderNumber() > column.getOrderNumber()).toList();

        log.debug("Columns to update: {}", columnsToUpdate);

        columnsToUpdate.forEach(c -> c.setOrderNumber(c.getOrderNumber() - 1));

        log.debug("Columns to update after: {}", columnsToUpdate);

        statusRepository.saveAll(columnsToUpdate);

        log.debug("Columns updated");

        statusRepository.delete(column);

        log.debug("Column deleted: {}", command.getStatusId());

        return null;
    }
}
