package dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.models.tokens.ProjectInviteToken;
import dev.farneser.tasktracker.api.repository.ProjectInviteTokenRepository;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProjectInviteTokenCommandHandler implements CommandHandler<CreateProjectInviteTokenCommand, Long> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteTokenRepository projectInviteTokenRepository;

    @Override
    public Long handle(CreateProjectInviteTokenCommand command)
            throws NotFoundException, OperationNotAuthorizedException {
        synchronized (this) {
            ProjectMember member = projectMemberRepository
                    .findByProjectIdAndMemberId(command.getProjectId(), command.getUserId())
                    .orElseThrow(() -> new ProjectMemberNotFoundException(command.getUserId()));

            if (!member.getRole().hasPermission(ProjectPermission.ADMIN_POST)) {
                throw new OperationNotAuthorizedException();
            }

            Optional<ProjectInviteToken> existingToken = projectInviteTokenRepository
                    .findByProjectId(command.getProjectId());

            if (existingToken.isPresent()) {
                return existingToken.get().getId();
            }

            ProjectInviteToken token = new ProjectInviteToken(member.getMember(), member.getProject());

            projectInviteTokenRepository.save(token);

            return token.getId();
        }
    }
}
