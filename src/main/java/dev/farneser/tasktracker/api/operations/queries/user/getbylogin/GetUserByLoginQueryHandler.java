package dev.farneser.tasktracker.api.operations.queries.user.getbylogin;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUserByLoginQueryHandler implements QueryHandler<GetUserByLoginQuery, UserView> {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserView handle(GetUserByLoginQuery query) throws NotFoundException {
        log.debug("Query: {}", query);

        Optional<User> userByName = userRepository.findByUsername(query.getLogin());
        Optional<User> userByEmail = userRepository.findByEmail(query.getLogin());

        User user = null;

        if (userByName.isPresent()) {
            user = userByName.get();
        } else if (userByEmail.isPresent()) {
            user = userByEmail.get();
        }

        if (user != null) {
            return modelMapper.map(user, UserView.class);
        }

        throw new UserNotFoundException(query.getLogin());
    }
}
