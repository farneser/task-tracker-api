package dev.farneser.tasktracker.api.operations.queries.kanbancolumn.getByUserId;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.KanbanColumnView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetKanbanColumnByUserIdQuery implements Query<List<KanbanColumnView>> {
    private Long userId;
}
