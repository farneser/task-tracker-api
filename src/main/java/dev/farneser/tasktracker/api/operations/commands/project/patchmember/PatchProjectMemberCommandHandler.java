package dev.farneser.tasktracker.api.operations.commands.project.patchmember;

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
public class PatchProjectMemberCommandHandler implements CommandHandler<PatchProjectMemberCommand, Long> {
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Long handle(PatchProjectMemberCommand command) throws NotFoundException, OperationNotAuthorizedException {
        Long projectId = command.getProjectId();
        Long adminId = command.getUserId();
        Long memberId = command.getMemberId();
        ProjectRole newRole = command.getRole();

        ProjectMember admin = findProjectMember(projectId, adminId);
        ProjectMember member = findProjectMember(projectId, memberId);

        if (member.getRole().hasPermission(ProjectPermission.CREATOR_PATCH)) {
            throw new OperationNotAuthorizedException("You can't change permission for this user");
        }

        if (admin.getRole() == ProjectRole.MEMBER) {
            throw new OperationNotAuthorizedException("You can't change permissions in this project");
        }

        if (admin.getRole() == ProjectRole.CREATOR && newRole == ProjectRole.CREATOR) {
            admin.setRole(ProjectRole.ADMIN);
        }

        if (admin.getRole() != ProjectRole.CREATOR || newRole != ProjectRole.CREATOR) {
            member.setRole(newRole);
        }

        projectMemberRepository.save(admin);
        projectMemberRepository.save(member);

        return member.getId();
    }

    private ProjectMember findProjectMember(Long projectId, Long memberId) throws NotFoundException {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId)
                .orElseThrow(() -> new NotFoundException("Project member not found"));
    }
}
