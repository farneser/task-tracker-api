package dev.farneser.tasktracker.api.operations.queries.project.getmembers;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectPermission;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProjectMembersQueryHandler
        implements QueryHandler<GetProjectMembersQuery, List<ProjectMemberView>> {
    private final ProjectRepository projectRepository;

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

        return project.getMembers().stream().map(ProjectMemberView::map).toList();
    }
}
