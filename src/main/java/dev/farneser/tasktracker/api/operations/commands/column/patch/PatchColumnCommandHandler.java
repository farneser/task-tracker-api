package dev.farneser.tasktracker.api.operations.commands.column.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import dev.farneser.tasktracker.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchColumnCommandHandler implements CommandHandler<PatchColumnCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(PatchColumnCommand command) throws NotFoundException {
        List<KanbanColumn> columns = columnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());
        KanbanColumn column = columns.stream().filter(c -> c.getId().equals(command.getColumnId())).findFirst().orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

        log.debug("Column found: {}", column);

        if (command.getColumnName() != null) {
            log.debug("Column name changed from {} to {}", column.getColumnName(), command.getColumnName());
            column.setColumnName(command.getColumnName());
        }

        if (command.getOrderNumber() != null) {
            log.debug("Column order number changed from {} to {}", column.getOrderNumber(), command.getOrderNumber());

            long oldOrder = column.getOrderNumber();
            long newOrder = command.getOrderNumber();

            column.setOrderNumber(newOrder);

            List<KanbanColumn> columnsToChange = columns.stream().filter(c -> c.getOrderNumber() >= Math.min(oldOrder, newOrder) && c.getOrderNumber() <= Math.max(oldOrder, newOrder)).toList();

            OrderService.patchOrder(column.getId(), newOrder, oldOrder, columnsToChange);
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
