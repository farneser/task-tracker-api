package dev.farneser.tasktracker.api.operations.queries.task.getarchivedbyuserid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetArchivedTaskByUserIdQuery implements Query<List<TaskLookupView>> {
    private Long userId;
}
