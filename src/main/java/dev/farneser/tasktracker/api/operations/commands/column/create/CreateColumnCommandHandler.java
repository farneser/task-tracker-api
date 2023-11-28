package dev.farneser.tasktracker.api.operations.commands.column.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CreateColumnCommandHandler implements CommandHandler<CreateColumnCommand, Long> {
    private final UserRepository userRepository;
    private final ColumnRepository columnRepository;

    @Override
    public Long handle(CreateColumnCommand command) throws NotFoundException {
        var user = userRepository.findById(command.getUserId()).orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        var columns = columnRepository.findByUserIdOrderByOrderNumber(user.getId()).orElse(new ArrayList<>());

        var orderNumber = 1L;

        if (!columns.isEmpty()) {
            orderNumber = columns.get(columns.size() - 1).getOrderNumber() + 1;
        }

        var creationDate = new Date(System.currentTimeMillis());

        var column = KanbanColumn
                .builder()
                .columnName(command.getColumnName())
                .isCompleted(command.getIsCompleted())
                .user(user)
                .orderNumber(orderNumber)
                .creatiionDate(creationDate)
                .editDate(creationDate)
                .build();

        columnRepository.save(column);

        return column.getId();
    }
}
