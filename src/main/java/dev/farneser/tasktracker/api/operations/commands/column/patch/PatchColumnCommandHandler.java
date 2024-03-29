package dev.farneser.tasktracker.api.operations.commands.column.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import dev.farneser.tasktracker.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchColumnCommandHandler implements CommandHandler<PatchColumnCommand, Void> {
    private final ColumnRepository columnRepository;
    private final Map<Long, Lock> userLocks = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public Void handle(PatchColumnCommand command) throws NotFoundException {
        Long userId = command.getUserId();
        Lock userLock = userLocks.computeIfAbsent(userId, k -> new ReentrantLock());

        userLock.lock();

        try {
            // FIXME 28.03.2024: change userid to project id
            List<Status> columns = columnRepository.findByProjectIdOrderByOrderNumber(userId).orElse(new ArrayList<>());
            Status column = columns.stream().filter(c -> c.getId().equals(command.getColumnId())).findFirst().orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found"));

            if (command.getColumnName() != null) {
                column.setStatusName(command.getColumnName());
            }

            if (command.getOrderNumber() != null) {
                log.debug("Column order number changed from {} to {}", column.getOrderNumber(), command.getOrderNumber());

                long oldOrder = column.getOrderNumber();
                long newOrder = command.getOrderNumber();

                column.setOrderNumber(newOrder);

                List<Status> columnsToChange = columns.stream().filter(c -> c.getOrderNumber() >= Math.min(oldOrder, newOrder) && c.getOrderNumber() <= Math.max(oldOrder, newOrder)).toList();

                OrderService.patchOrder(column.getId(), newOrder, oldOrder, columnsToChange);
            }

            if (command.getIsCompleted() != null) {
                log.debug("Column isCompleted changed from {} to {}", column.getIsCompleted(), command.getIsCompleted());
                column.setIsCompleted(command.getIsCompleted());
            }

            log.debug("Column edit date changed from {} to {}", column.getEditDate(), new Date(System.currentTimeMillis()));
            column.setEditDate(new Date(System.currentTimeMillis()));

            columnRepository.save(column);
        } finally {
            userLock.unlock();
        }

        return null;
    }
}
