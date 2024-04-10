package dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.accept;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import dev.farneser.tasktracker.api.models.tokens.ProjectInviteToken;
import dev.farneser.tasktracker.api.repository.ProjectInviteTokenRepository;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.ProjectRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AcceptProjectInviteTokenCommandHandler implements CommandHandler<AcceptProjectInviteTokenCommand, Long> {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteTokenRepository projectInviteTokenRepository;

    @Override
    public Long handle(AcceptProjectInviteTokenCommand command)
            throws NotFoundException, OperationNotAuthorizedException {
        synchronized (this) {

            ProjectInviteToken existingToken = projectInviteTokenRepository
                    .findByToken(command.getToken())
                    .orElseThrow(() -> new NotFoundException(""));

            Project project = projectRepository
                    .findById(existingToken.getProject().getId())
                    .orElseThrow(() -> new NotFoundException(""));

            Optional<ProjectMember> projectMember = projectMemberRepository
                    .findProjectMemberByProjectIdAndMemberId(project.getId(), command.getUserId());

            if (projectMember.isPresent()) {
                return projectMember.get().getId();
            }

            User user = userRepository
                    .findById(command.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(command.getUserId()));

            ProjectMember member = new ProjectMember();

            member.setProject(project);
            member.setMember(user);
            member.setRole(ProjectRole.MEMBER);

            projectMemberRepository.save(member);

            return member.getId();
        }
    }
}
