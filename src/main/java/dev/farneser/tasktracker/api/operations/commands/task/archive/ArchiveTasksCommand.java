package dev.farneser.tasktracker.api.operations.commands.task.archive;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArchiveTasksCommand implements Command<Void> {
    private Long userId;
}
