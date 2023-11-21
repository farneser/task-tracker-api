package dev.farneser.tasktracker.api.operations.commands.column.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteColumnCommandHandler implements CommandHandler<DeleteColumnCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(DeleteColumnCommand command) throws NotFoundException {
        var column = columnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

        columnRepository.delete(column);

        return null;
    }
}
