package dev.farneser.tasktracker.api.operations.commands.project.leave;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveProjectCommandHandler implements CommandHandler<LeaveProjectCommand, Void> {
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Void handle(LeaveProjectCommand command)
            throws NotFoundException, OperationNotAuthorizedException {

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(command.getProjectId(), command.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (member.getRole() == ProjectRole.CREATOR) {
            throw new OperationNotAuthorizedException("Failed to leave project: you are project creator");
        }

        member.getProject().getStatuses().forEach(s -> s.getTasks().forEach(t -> {
            if (t.getAssignedFor().getId().equals(member.getMember().getId())) {
                t.setAssignedFor(null);
            }
        }));

        projectMemberRepository.delete(member);

        return null;
    }
}
