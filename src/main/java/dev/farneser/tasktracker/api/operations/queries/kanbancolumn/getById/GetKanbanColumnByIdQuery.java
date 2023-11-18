package dev.farneser.tasktracker.api.operations.queries.kanbancolumn.getById;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.view.KanbanColumnView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetKanbanColumnByIdQuery implements Query<KanbanColumnView> {
    private Long userId;
    private Long columnId;
}
