package dev.farneser.tasktracker.api.operations.commands.task.create;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskCommand implements Command<Long> {
    private Long userId;
    private Long statusId;
    private Long assignedFor;
    private String taskName;
    private String description;
}
