package dev.farneser.tasktracker.api.operations.commands.status.delete;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteStatusCommand implements Command<Void> {
    private Long userId;
    private Long statusId;
}
