package dev.farneser.tasktracker.api.operations.commands.project.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteProjectCommandHandler implements CommandHandler<DeleteProjectCommand, Void> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectInviteTokenRepository inviteTokenRepository;
    private final StatusRepository statusRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Void handle(DeleteProjectCommand command) throws NotFoundException, OperationNotAuthorizedException {
        ProjectMember member = projectMemberRepository
                .findByProjectIdAndMemberId(command.getProjectId(), command.getMemberId())
                .orElseThrow(() -> new ProjectMemberNotFoundException(command.getMemberId()));

        if (!member.getRole().hasPermission(ProjectPermission.CREATOR_DELETE)) {
            throw new OperationNotAuthorizedException();
        }

        taskRepository.deleteAll(member.getProject().getStatuses().stream()
                .flatMap(status -> status.getTasks().stream())
                .collect(Collectors.toList())
        );

        statusRepository.deleteAll(member.getProject().getStatuses());

        projectMemberRepository.deleteAll(member.getProject().getMembers());

        inviteTokenRepository.deleteByProjectId(member.getProject().getId());

        projectRepository.deleteById(command.getProjectId());

        return null;
    }
}
