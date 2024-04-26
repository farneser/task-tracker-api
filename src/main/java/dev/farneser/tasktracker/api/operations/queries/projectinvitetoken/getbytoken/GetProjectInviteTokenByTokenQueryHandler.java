package dev.farneser.tasktracker.api.operations.queries.projectinvitetoken.getbytoken;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.tokens.ProjectInviteToken;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import dev.farneser.tasktracker.api.repository.ProjectInviteTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectInviteTokenByTokenQueryHandler implements QueryHandler<GetProjectInviteTokenByTokenQuery, ProjectInviteTokenView> {
    private final ProjectInviteTokenRepository projectInviteTokenRepository;
    private final ModelMapper mapper;

    @Override
    public ProjectInviteTokenView handle(GetProjectInviteTokenByTokenQuery query)
            throws NotFoundException, OperationNotAuthorizedException {
        ProjectInviteToken token = projectInviteTokenRepository
                .findByToken(query.getToken())
                .orElseThrow(() -> new NotFoundException("Project invite token not found"));

        ProjectInviteTokenView view = mapper.map(token, ProjectInviteTokenView.class);

        view.setEmail(token.getUser().getEmail());
        view.setProjectName(token.getProject().getProjectName());

        return view;
    }
}
