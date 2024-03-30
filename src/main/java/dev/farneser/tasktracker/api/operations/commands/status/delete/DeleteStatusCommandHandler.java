package dev.farneser.tasktracker.api.operations.commands.status.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteStatusCommandHandler implements CommandHandler<DeleteStatusCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(DeleteStatusCommand command) throws NotFoundException {
        Status column = columnRepository.findByIdAndUserId(command.getStatusId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getStatusId() + " not found"));

        log.debug("Column found: {}", column);

        List<Status> columns = columnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        log.debug("Columns found: {}", columns);

        List<Status> columnsToUpdate = columns.stream().filter(c -> c.getOrderNumber() > column.getOrderNumber()).toList();

        log.debug("Columns to update: {}", columnsToUpdate);

        columnsToUpdate.forEach(c -> c.setOrderNumber(c.getOrderNumber() - 1));

        log.debug("Columns to update after: {}", columnsToUpdate);

        columnRepository.saveAll(columnsToUpdate);

        log.debug("Columns updated");

        columnRepository.delete(column);

        log.debug("Column deleted: {}", command.getStatusId());

        return null;
    }
}
