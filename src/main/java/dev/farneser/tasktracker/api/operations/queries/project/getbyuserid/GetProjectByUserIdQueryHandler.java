package dev.farneser.tasktracker.api.operations.queries.project.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectByUserIdQueryHandler implements QueryHandler<GetProjectByUserIdQuery, List<ProjectView>> {
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProjectView> handle(GetProjectByUserIdQuery query) throws NotFoundException {
        List<ProjectMember> projectMembers = projectMemberRepository.findByMemberId(query.getUserId()).orElse(new ArrayList<>());

        return projectMembers.stream().map(p -> {
            ProjectView view = modelMapper.map(p.getProject(), ProjectView.class);

            view.setRole(p.getRole());

            return view;
        }).toList();
    }
}
