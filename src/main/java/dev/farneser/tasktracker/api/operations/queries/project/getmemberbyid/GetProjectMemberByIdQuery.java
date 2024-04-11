package dev.farneser.tasktracker.api.operations.queries.project.getmemberbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetProjectMemberByIdQuery implements Query<ProjectMemberView> {
    private Long id;
    private Long userId;
    private Long projectId;
}
