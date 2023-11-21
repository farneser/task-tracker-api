package dev.farneser.tasktracker.api.operations.commands.kanbantask.delete;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteKanbanTaskCommand implements Command<Void> {
    private Long userId;
    private Long taskId;
}
