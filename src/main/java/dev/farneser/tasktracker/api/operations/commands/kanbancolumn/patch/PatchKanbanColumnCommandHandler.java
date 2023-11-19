package dev.farneser.tasktracker.api.operations.commands.kanbancolumn.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.operations.views.KanbanColumnView;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatchKanbanColumnCommandHandler implements CommandHandler<PatchKanbanColumnCommand, KanbanColumnView> {
    private final KanbanColumnRepository kanbanColumnRepository;
    private final ModelMapper modelMapper;

    @Override
    public KanbanColumnView handle(PatchKanbanColumnCommand command) throws NotFoundException {
        var column = kanbanColumnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

        if (command.getColumnName() != null) {
            column.setColumnName(command.getColumnName());
        }

        if (command.getOrderNumber() != null) {
            column.setOrderNumber(command.getOrderNumber());
        }

        if (command.getIsCompleted() != null) {
            column.setIsCompleted(command.getIsCompleted());
        }

        kanbanColumnRepository.save(column);

        return modelMapper.map(column, KanbanColumnView.class);
    }
}
