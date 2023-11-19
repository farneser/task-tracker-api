package dev.farneser.tasktracker.api.operations.commands.kanbancolumn.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteKanbanColumnCommandHandler implements CommandHandler<DeleteKanbanColumnCommand, Void> {
    private final KanbanColumnRepository kanbanColumnRepository;

    @Override
    public Void handle(DeleteKanbanColumnCommand command) throws NotFoundException {
        var column = kanbanColumnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

        kanbanColumnRepository.delete(column);

        return null;
    }
}
