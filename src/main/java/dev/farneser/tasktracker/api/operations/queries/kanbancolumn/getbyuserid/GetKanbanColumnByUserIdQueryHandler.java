package dev.farneser.tasktracker.api.operations.queries.kanbancolumn.getbyuserid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.KanbanColumnView;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetKanbanColumnByUserIdQueryHandler implements QueryHandler<GetKanbanColumnByUserIdQuery, List<KanbanColumnView>> {
    private final KanbanColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<KanbanColumnView> handle(GetKanbanColumnByUserIdQuery query) throws NotFoundException {
        var user = userRepository.findById(query.getUserId()).orElseThrow(() -> new UserNotFoundException(query.getUserId()));

        var column = columnRepository.findByUserId(user.getId()).orElse(new ArrayList<>());

        return column.stream().map(c -> modelMapper.map(c, KanbanColumnView.class)).collect(Collectors.toList());
    }
}
