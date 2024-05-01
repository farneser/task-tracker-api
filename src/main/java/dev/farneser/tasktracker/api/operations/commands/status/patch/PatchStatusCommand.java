package dev.farneser.tasktracker.api.operations.commands.status.patch;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchStatusCommand implements Command<Void> {
    private Long userId;
    private Long statusId;
    @Size(min = 1, max = 255)
    private String statusName;
    @Size(min = 1, max = 7)
    private String statusColor;
    private Boolean isCompleted;
    private Long orderNumber;
}
