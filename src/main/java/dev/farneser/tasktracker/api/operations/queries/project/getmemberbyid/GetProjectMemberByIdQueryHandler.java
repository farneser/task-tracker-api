package dev.farneser.tasktracker.api.operations.queries.project.getmemberbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectMemberByIdQueryHandler implements QueryHandler<GetProjectMemberByIdQuery, ProjectMemberView> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper mapper;

    @Override
    public ProjectMemberView handle(GetProjectMemberByIdQuery query)
            throws NotFoundException, OperationNotAuthorizedException {

        ProjectMember user = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(query.getProjectId(), query.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getRole().hasPermission(ProjectPermission.USER_GET)) {
            throw new OperationNotAuthorizedException();
        }

        ProjectMember member = projectMemberRepository
                .findById(query.getId())
                .orElseThrow(() -> new NotFoundException("Member not found"));

        return mapper.map(member, ProjectMemberView.class);
    }
}
