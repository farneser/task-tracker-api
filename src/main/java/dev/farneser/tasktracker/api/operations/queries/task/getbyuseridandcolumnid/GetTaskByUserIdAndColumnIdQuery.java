package dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandcolumnid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetTaskByUserIdAndColumnIdQuery implements Query<List<TaskView>> {
    private Long userId;
    private Long columnId;
}
