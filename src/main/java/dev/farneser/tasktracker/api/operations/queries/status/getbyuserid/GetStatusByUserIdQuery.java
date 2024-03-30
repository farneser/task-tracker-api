package dev.farneser.tasktracker.api.operations.queries.status.getbyuserid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetStatusByUserIdQuery implements Query<List<StatusView>> {
    private Long userId;
    private Boolean retrieveTasks;
}
