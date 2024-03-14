package dev.farneser.tasktracker.api.operations.queries.user.getbyemail;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUserByEmailQueryHandler implements QueryHandler<GetUserByEmailQuery, UserView> {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserView handle(GetUserByEmailQuery query) throws NotFoundException {
        log.debug("Query: {}", query);

        User user = userRepository.findByEmail(query.getEmail()).orElseThrow(() -> new UserNotFoundException(query.getEmail()));

        log.debug("User found: {}", user);

        return modelMapper.map(user, UserView.class);
    }
}
