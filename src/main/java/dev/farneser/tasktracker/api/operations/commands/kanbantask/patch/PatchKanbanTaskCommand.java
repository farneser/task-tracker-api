package dev.farneser.tasktracker.api.operations.commands.kanbantask.patch;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchKanbanTaskCommand implements Command<Void> {
    private Long userId;
    private Long taskId;
    private Long columnId;
    private String title;
    private String description;
    private Long orderNumber;
}
