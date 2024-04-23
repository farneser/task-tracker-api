package dev.farneser.tasktracker.api.operations.commands.project.deletemember;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteProjectMemberCommandHandler implements CommandHandler<DeleteProjectMemberCommand, Void> {
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Void handle(DeleteProjectMemberCommand command) throws NotFoundException, OperationNotAuthorizedException {
        ProjectMember admin = findProjectMember(command.getProjectId(), command.getUserId());
        ProjectMember member = findProjectMember(command.getProjectId(), command.getMemberId());

        if (admin.getRole().hasPermission(ProjectPermission.ADMIN_DELETE)) {
            throw new OperationNotAuthorizedException("You can't delete users in this project");
        }

        if (member.getRole() != ProjectRole.MEMBER) {
            throw new OperationNotAuthorizedException("You can't delete user with role: " + member.getRole());
        }

        projectMemberRepository.save(member);

        return null;
    }

    private ProjectMember findProjectMember(Long projectId, Long memberId) throws NotFoundException {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId)
                .orElseThrow(() -> new NotFoundException("Project member not found"));
    }
}
