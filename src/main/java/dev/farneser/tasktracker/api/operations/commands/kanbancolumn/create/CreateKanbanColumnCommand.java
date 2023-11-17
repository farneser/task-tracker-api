package dev.farneser.tasktracker.api.operations.commands.kanbancolumn.create;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateKanbanColumnCommand implements Command<Long> {
    private Long userId;
    private String columnName;
    private Boolean isCompleted;
}