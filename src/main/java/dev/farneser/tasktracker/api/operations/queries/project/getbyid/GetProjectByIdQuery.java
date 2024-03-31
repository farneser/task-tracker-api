package dev.farneser.tasktracker.api.operations.queries.project.getbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetProjectByIdQuery implements Query<ProjectView> {
    private Long userId;
    private Long projectId;
}
