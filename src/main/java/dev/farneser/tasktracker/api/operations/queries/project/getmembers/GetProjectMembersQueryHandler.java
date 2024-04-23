package dev.farneser.tasktracker.api.operations.queries.project.getmembers;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectMembersQueryHandler
        implements QueryHandler<GetProjectMembersQuery, List<ProjectMemberView>> {
    private final ProjectRepository projectRepository;
    private final ModelMapper mapper;

    @Override
    public List<ProjectMemberView> handle(GetProjectMembersQuery query)
            throws NotFoundException, OperationNotAuthorizedException {
        Project project = projectRepository
                .findById(query.getProjectId())
                .orElseThrow(() -> new NotFoundException(""));

        for (ProjectMember member : project.getMembers()) {
            if (member.getMember().getId().equals(query.getUserId())) {
                if (!member.getRole().hasPermission(ProjectPermission.USER_GET)) {
                    throw new OperationNotAuthorizedException();
                }

                break;
            }
        }

        return project.getMembers().stream().map(p -> {
            ProjectMemberView view = mapper.map(p, ProjectMemberView.class);
            view.setMember(mapper.map(p.getMember(), UserView.class));
            view.setProjectId(p.getProject().getId());
            return view;
        }).toList();
    }
}
