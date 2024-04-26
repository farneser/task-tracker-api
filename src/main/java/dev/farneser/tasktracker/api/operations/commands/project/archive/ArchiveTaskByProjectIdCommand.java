package dev.farneser.tasktracker.api.operations.commands.project.archive;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArchiveTaskByProjectIdCommand implements Command<Void> {
    private Long userId;
    private Long projectId;
}
