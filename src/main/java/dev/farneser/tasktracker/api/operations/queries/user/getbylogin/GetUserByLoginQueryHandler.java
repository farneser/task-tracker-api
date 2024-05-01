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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUserByLoginQueryHandler implements QueryHandler<GetUserByLoginQuery, UserView> {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserView handle(GetUserByLoginQuery query) throws NotFoundException {
        log.debug("Query: {}", query);

        ArrayList<Function<String, Optional<User>>> findUserFunctions = new ArrayList<>();

        findUserFunctions.add(userRepository::findByUsername);
        findUserFunctions.add(userRepository::findByEmail);

        for (Function<String, Optional<User>> queryFunction : findUserFunctions) {
            Optional<User> userOptional = queryFunction.apply(query.getLogin());

            if (userOptional.isPresent()) {
                return modelMapper.map(userOptional.get(), UserView.class);
            }
        }

        throw new UserNotFoundException(query.getLogin());
    }
}
