package dev.farneser.tasktracker.api.operations.commands.column.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteColumnCommandHandler implements CommandHandler<DeleteColumnCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(DeleteColumnCommand command) throws NotFoundException {
        var column = columnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

        log.debug("Column found: {}", column);

        var columns = columnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        log.debug("Columns found: {}", columns);

        var columnsToUpdate = columns.stream().filter(c -> c.getOrderNumber() > column.getOrderNumber()).toList();

        log.debug("Columns to update: {}", columnsToUpdate);

        columnsToUpdate.forEach(c -> c.setOrderNumber(c.getOrderNumber() - 1));

        log.debug("Columns to update after: {}", columnsToUpdate);

        columnRepository.saveAll(columnsToUpdate);

        log.debug("Columns updated");

        columnRepository.delete(column);

        log.debug("Column deleted: {}", command.getColumnId());

        return null;
    }
}
