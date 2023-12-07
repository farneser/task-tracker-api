package dev.farneser.tasktracker.api.operations.queries.user.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUserByIdQueryHandler implements QueryHandler<GetUserByIdQuery, UserView> {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserView handle(GetUserByIdQuery query) throws NotFoundException {
        log.debug("Query: {}", query);

        var user = userRepository.findById(query.getId()).orElseThrow(() -> new UserNotFoundException(query.getId()));

        log.debug("User found: {}", user);

        return modelMapper.map(user, UserView.class);
    }
}
