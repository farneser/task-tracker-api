package dev.farneser.tasktracker.api.operations.commands.project.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteProjectCommandHandler implements CommandHandler<DeleteProjectCommand, Void> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public Void handle(DeleteProjectCommand command) throws NotFoundException, OperationNotAuthorizedException {
        // FIXME 27.03.2024 write exception
        ProjectMember member = projectMemberRepository.findByProjectIdAndMemberId(command.getProjectId(), command.getMemberId()).orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.CREATOR_DELETE)) {
            throw new OperationNotAuthorizedException();
        }

        projectRepository.deleteById(command.getProjectId());

        return null;
    }
}
