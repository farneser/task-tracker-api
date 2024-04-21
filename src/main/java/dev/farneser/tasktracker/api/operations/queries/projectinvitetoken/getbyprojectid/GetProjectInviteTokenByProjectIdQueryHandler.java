package dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyprojectid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.models.tokens.ProjectInviteToken;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import dev.farneser.tasktracker.api.repository.ProjectInviteTokenRepository;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectInviteTokenByProjectIdQueryHandler
        implements QueryHandler<GetProjectInviteTokenByProjectIdQuery, ProjectInviteTokenView> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteTokenRepository projectInviteTokenRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProjectInviteTokenView handle(GetProjectInviteTokenByProjectIdQuery query)
            throws NotFoundException, OperationNotAuthorizedException {
        ProjectInviteToken token = projectInviteTokenRepository
                .findByProjectId(query.getProjectId())
                .orElseThrow(() -> new NotFoundException(""));

        ProjectMember member = projectMemberRepository
                .findProjectMemberByProjectIdAndMemberId(token.getProject().getId(), query.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_GET)) {
            throw new OperationNotAuthorizedException();
        }

        return modelMapper.map(token, ProjectInviteTokenView.class);
    }
}
