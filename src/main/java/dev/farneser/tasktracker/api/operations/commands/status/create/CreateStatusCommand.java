package dev.farneser.tasktracker.api.operations.commands.status.create;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStatusCommand implements Command<Long> {
    private Long userId;
    private String statusName;
    private Boolean isCompleted;
    private Long projectId;
}
