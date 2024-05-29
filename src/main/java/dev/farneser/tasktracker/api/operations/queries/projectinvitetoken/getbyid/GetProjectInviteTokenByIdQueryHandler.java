package dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
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
public class GetProjectInviteTokenByIdQueryHandler implements QueryHandler<GetProjectInviteTokenByIdQuery, ProjectInviteTokenView> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteTokenRepository projectInviteTokenRepository;
    private final ModelMapper mapper;

    @Override
    public ProjectInviteTokenView handle(GetProjectInviteTokenByIdQuery query)
            throws NotFoundException, OperationNotAuthorizedException {
        ProjectInviteToken token = projectInviteTokenRepository
                .findById(query.getTokenId())
                .orElseThrow(() -> new ProjectMemberNotFoundException(query.getUserId()));

        return getProjectInviteTokenView(token, projectMemberRepository, query.getUserId(), mapper);
    }

    public static ProjectInviteTokenView getProjectInviteTokenView(
            ProjectInviteToken token,
            ProjectMemberRepository projectMemberRepository,
            Long userId,
            ModelMapper mapper
    ) throws NotFoundException, OperationNotAuthorizedException {
        ProjectMember member = projectMemberRepository
                .findByProjectIdAndMemberId(token.getProject().getId(), userId)
                .orElseThrow(() -> new ProjectMemberNotFoundException(userId));

        if (!member.getRole().hasPermission(ProjectPermission.ADMIN_GET)) {
            throw new OperationNotAuthorizedException("You do not have permissions to access invite token.");
        }

        ProjectInviteTokenView view = mapper.map(token, ProjectInviteTokenView.class);

        view.setEmail(token.getUser().getEmail());
        view.setProjectName(token.getProject().getProjectName());

        return view;
    }
}
