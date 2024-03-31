package dev.farneser.tasktracker.api.operations.commands.status.patch;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchStatusCommand implements Command<Void> {
    private Long userId;
    private Long statusId;
    private Long projectId;
    private String statusName;
    private Boolean isCompleted;
    private Long orderNumber;
}
