package dev.farneser.tasktracker.api.operations.queries.project.getmembers;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetProjectMembersQuery implements Query<List<ProjectMemberView>> {
    private Long userId;
    private Long projectId;
}
