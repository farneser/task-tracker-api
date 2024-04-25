package dev.farneser.tasktracker.api.operations.queries.project.getmemberbyidanduserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectMemberByIdAndUserIdQueryHandler implements QueryHandler<GetProjectMemberByIdAndUserIdQuery, ProjectMemberView> {
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public ProjectMemberView handle(GetProjectMemberByIdAndUserIdQuery query)
            throws NotFoundException, OperationNotAuthorizedException {

        ProjectMember user = projectMemberRepository
                .findByIdAndMemberId(query.getProjectMemberId(), query.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getRole().hasPermission(ProjectPermission.USER_GET)) {
            throw new OperationNotAuthorizedException();
        }

        return ProjectMemberView.map(user);
    }
}
