package dev.farneser.tasktracker.api.operations.commands.column.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchColumnCommandHandler implements CommandHandler<PatchColumnCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(PatchColumnCommand command) throws NotFoundException {
        var columns = columnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());
        var column = columns.stream().filter(c -> c.getId().equals(command.getColumnId())).findFirst().orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

        log.debug("Column found: {}", column);

        if (command.getColumnName() != null) {
            log.debug("Column name changed from {} to {}", column.getColumnName(), command.getColumnName());
            column.setColumnName(command.getColumnName());
        }

        if (command.getOrderNumber() != null) {
            log.debug("Column order number changed from {} to {}", column.getOrderNumber(), command.getOrderNumber());

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
            log.debug("Column isCompleted changed from {} to {}", column.getIsCompleted(), command.getIsCompleted());
            column.setIsCompleted(command.getIsCompleted());
        }
        log.debug("Column edit date changed from {} to {}", column.getEditDate(), new Date(System.currentTimeMillis()));
        column.setEditDate(new Date(System.currentTimeMillis()));

        log.debug("Column saved: {}", column);

        columnRepository.save(column);

        log.debug("Column saved: {}", column);
        return null;
    }
}
