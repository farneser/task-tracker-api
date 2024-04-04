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

        Status status = statusRepository
                .findById(command.getStatusId())
                .orElseThrow(() -> new NotFoundException("Status with id " + command.getStatusId() + " not found"));

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(status.getProject().getId(), command.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_DELETE)) {
            throw new OperationNotAuthorizedException();
        }

        log.debug("Status found: {}", status);

        List<Status> statuses = statusRepository.findByProjectIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        log.debug("Statuses found: {}", statuses);

        List<Status> statusesToUpdate = statuses.stream().filter(c -> c.getOrderNumber() > status.getOrderNumber()).toList();

        log.debug("Statuses to update: {}", statusesToUpdate);

        statusesToUpdate.forEach(c -> c.setOrderNumber(c.getOrderNumber() - 1));

        log.debug("Statuses to update after: {}", statusesToUpdate);

        statusRepository.saveAll(statusesToUpdate);

        log.debug("Statuses updated");

        statusRepository.delete(status);

        log.debug("Status deleted: {}", command.getStatusId());

        return null;
    }
}
