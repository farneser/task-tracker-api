package dev.farneser.tasktracker.api.operations.commands.kanbancolumn.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchKanbanColumnCommandHandler implements CommandHandler<PatchKanbanColumnCommand, Void> {
    private final KanbanColumnRepository kanbanColumnRepository;

    @Override
    public Void handle(PatchKanbanColumnCommand command) throws NotFoundException {
        var columns = kanbanColumnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());
        var column = columns.stream().filter(c -> c.getId().equals(command.getColumnId())).findFirst().orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

        if (command.getColumnName() != null) {
            column.setColumnName(command.getColumnName());
        }

        if (command.getOrderNumber() != null) {
            var oldOrder = column.getOrderNumber();
            var newOrder = command.getOrderNumber();

            column.setOrderNumber(newOrder);

            var columnsToChange = columns.stream().filter(c -> c.getOrderNumber() >= Math.min(oldOrder, newOrder) && c.getOrderNumber() <= Math.max(oldOrder, newOrder)).toList();

            columnsToChange.forEach(c -> {
                if (c.getId().equals(column.getId())) {
                    return;
                }

                if (oldOrder < newOrder) {
                    c.setOrderNumber(c.getOrderNumber() - 1);
                } else {
                    c.setOrderNumber(c.getOrderNumber() + 1);
                }
            });
        }

        if (command.getIsCompleted() != null) {
            column.setIsCompleted(command.getIsCompleted());
        }

        kanbanColumnRepository.save(column);

        return null;
    }
}
