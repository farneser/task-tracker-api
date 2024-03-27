package dev.farneser.tasktracker.api.operations.queries.project.getbyuserid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetProjectByUserIdQuery implements Query<List<ProjectView>> {
    private Long userId;
}
