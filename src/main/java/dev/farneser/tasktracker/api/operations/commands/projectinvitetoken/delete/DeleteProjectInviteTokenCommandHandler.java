package dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ProjectInviteTokenRepository;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteProjectInviteTokenCommandHandler implements CommandHandler<DeleteProjectInviteTokenCommand, Void> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteTokenRepository projectInviteTokenRepository;

    @Override
    @Transactional
    public Void handle(DeleteProjectInviteTokenCommand command)
            throws NotFoundException, OperationNotAuthorizedException {
        ProjectMember member = projectMemberRepository
                .findByProjectIdAndMemberId(command.getProjectId(), command.getUserId())
                .orElseThrow(() -> new ProjectMemberNotFoundException(command.getUserId()));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_DELETE)) {
            throw new OperationNotAuthorizedException();
        }

        Long deletedCount = projectInviteTokenRepository.deleteByProjectId(command.getProjectId());

        if (deletedCount == 0) {
            throw new NotFoundException("Project invite token in project:" + command.getProjectId() + " not found");
        }

        return null;
    }
}
