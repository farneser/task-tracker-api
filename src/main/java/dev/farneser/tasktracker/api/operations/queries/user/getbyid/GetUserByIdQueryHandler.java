package dev.farneser.tasktracker.api.operations.queries.user.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserByIdQueryHandler implements QueryHandler<GetUserByIdQuery, UserView> {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserView handle(GetUserByIdQuery query) throws NotFoundException {
        var user = userRepository.findById(query.getId()).orElseThrow(() -> new UserNotFoundException(query.getId()));

        return modelMapper.map(user, UserView.class);
    }
}
