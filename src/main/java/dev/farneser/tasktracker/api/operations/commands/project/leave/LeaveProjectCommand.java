package dev.farneser.tasktracker.api.operations.commands.project.leave;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveProjectCommand implements Command<Void> {
    private Long userId;
    private Long projectId;
}
