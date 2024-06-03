package dev.farneser.tasktracker.api.operations.commands.task.patch;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchTaskCommand implements Command<Void> {
    private Long userId;
    private Long taskId;
    private Long statusId;
    private Long assignedFor;
    @Size(min = 1, max = 255)
    private String taskName;
    private String description;
    private Long orderNumber;
}
