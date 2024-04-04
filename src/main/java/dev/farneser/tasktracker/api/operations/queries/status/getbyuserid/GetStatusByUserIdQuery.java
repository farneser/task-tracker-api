package dev.farneser.tasktracker.api.operations.queries.status.getbyuserid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * This query returns a list of statuses by project id and when project id is -1 returns all user statuses
 */
@Data
@AllArgsConstructor
public class GetStatusByUserIdQuery implements Query<List<StatusView>> {
    private Long userId;
    private Long projectId;
    private Boolean retrieveTasks;
}
