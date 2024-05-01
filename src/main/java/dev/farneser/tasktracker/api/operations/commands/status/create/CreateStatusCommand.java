package dev.farneser.tasktracker.api.operations.commands.status.create;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStatusCommand implements Command<Long> {
    private Long userId;
    private Long projectId;
    @Size(min = 1, max = 255)
    private String statusName;
    @Size(min = 1, max = 7)
    private String statusColor;
    private Boolean isCompleted;
}
