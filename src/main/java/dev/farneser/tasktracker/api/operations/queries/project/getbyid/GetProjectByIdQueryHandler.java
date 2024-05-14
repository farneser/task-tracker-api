package dev.farneser.tasktracker.api.operations.queries.project.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.ProjectMemberNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectByIdQueryHandler implements QueryHandler<GetProjectByIdQuery, ProjectView> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper mapper;

    @Override
    public ProjectView handle(GetProjectByIdQuery query) throws NotFoundException {
        ProjectMember member = projectMemberRepository
                .findByProjectIdAndMemberId(query.getProjectId(), query.getUserId())
                .orElseThrow(() -> new ProjectMemberNotFoundException(query.getUserId()));

        ProjectView view = mapper.map(member.getProject(), ProjectView.class);

        view.setRole(member.getRole());

        return view;
    }
}
