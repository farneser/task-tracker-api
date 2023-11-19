package dev.farneser.tasktracker.api.operations.commands.kanbancolumn.delete;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteKanbanColumnCommand implements Command<Void> {
    private Long userId;
    private Long columnId;
}
