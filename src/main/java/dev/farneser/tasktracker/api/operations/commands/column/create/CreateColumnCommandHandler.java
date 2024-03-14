package dev.farneser.tasktracker.api.operations.commands.column.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateColumnCommandHandler implements CommandHandler<CreateColumnCommand, Long> {
    private final UserRepository userRepository;
    private final ColumnRepository columnRepository;

    @Override
    public Long handle(CreateColumnCommand command) throws NotFoundException {
        User user = userRepository.findById(command.getUserId()).orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        log.debug("User found: {}", user);

        List<KanbanColumn> columns = columnRepository.findByUserIdOrderByOrderNumber(user.getId()).orElse(new ArrayList<>());

        log.debug("Columns found: {}", columns);

        long orderNumber = 1L;

        if (!columns.isEmpty()) {
            orderNumber = columns.get(columns.size() - 1).getOrderNumber() + 1;
        }

        log.debug("Order number: {}", orderNumber);

        Date creationDate = new Date(System.currentTimeMillis());

        KanbanColumn column = KanbanColumn
                .builder()
                .columnName(command.getColumnName())
                .isCompleted(command.getIsCompleted())
                .user(user)
                .orderNumber(orderNumber)
                .creationDate(creationDate)
                .editDate(creationDate)
                .build();

        log.debug("Column created: {}", column);

        columnRepository.save(column);

        log.debug("Column saved: {}", column);

        return column.getId();
    }
}
