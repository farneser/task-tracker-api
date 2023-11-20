package dev.farneser.tasktracker.api.operations.commands.kanbancolumn.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CreateKanbanColumnCommandHandler implements CommandHandler<CreateKanbanColumnCommand, Long> {
    private final UserRepository userRepository;
    private final KanbanColumnRepository kanbanColumnRepository;

    @Override
    public Long handle(CreateKanbanColumnCommand command) throws NotFoundException {
        var user = userRepository.findById(command.getUserId()).orElseThrow(() -> new UserNotFoundException(command.getUserId()));

        var columns = kanbanColumnRepository.findByUserIdOrderByOrderNumber(user.getId()).orElse(new ArrayList<>());

        var orderNumber = 1L;

        if (!columns.isEmpty()) {
            orderNumber = columns.get(columns.size() - 1).getOrderNumber() + 1;
        }

        var column = KanbanColumn
                .builder()
                .columnName(command.getColumnName())
                .isCompleted(command.getIsCompleted())
                .user(user)
                .orderNumber(orderNumber)
                .build();

        kanbanColumnRepository.save(column);

        return column.getId();
    }
}
