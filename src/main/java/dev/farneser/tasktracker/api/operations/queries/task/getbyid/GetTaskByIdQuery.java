package dev.farneser.tasktracker.api.operations.queries.task.getbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetTaskByIdQuery implements Query<TaskView> {
    private Long userId;
    private Long taskId;
}
