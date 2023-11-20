package dev.farneser.tasktracker.api.operations.commands.kanbancolumn.patch;

import dev.farneser.tasktracker.api.mediator.Command;
import dev.farneser.tasktracker.api.operations.views.KanbanColumnView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchKanbanColumnCommand implements Command<KanbanColumnView> {
    private Long userId;
    private Long columnId;
    private String columnName;
    private Boolean isCompleted;
    private Long orderNumber;
}
