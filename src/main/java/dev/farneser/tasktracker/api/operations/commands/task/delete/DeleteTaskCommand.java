package dev.farneser.tasktracker.api.operations.commands.task.delete;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTaskCommand implements Command<Void> {
    private Long userId;
    private Long taskId;
}
