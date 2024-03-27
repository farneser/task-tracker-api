package dev.farneser.tasktracker.api.operations.commands.project.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchProjectCommandHandler implements CommandHandler<PatchProjectCommand, Void> {
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Void handle(PatchProjectCommand command) throws NotFoundException, OperationNotAuthorizedException {
        // FIXME 27.03.2024 write exception
        ProjectMember member = projectMemberRepository.findProjectMemberByProjectIdAndMemberId(command.getProjectId(), command.getUserId()).orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_PATCH)) {
            throw new OperationNotAuthorizedException();
        }

        if (command.getProjectName() != null) {
            member.getProject().setProjectName(command.getProjectName());
        }

        projectMemberRepository.save(member);

        return null;
    }
}
