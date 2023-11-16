package dev.farneser.tasktracker.api.operations.queries.user.getbyemail;

import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetUserByEmailQueryHandler implements QueryHandler<GetUserByEmailQuery, User> {
    private final UserRepository userRepository;

    @Override
    public User handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with email: " + query.getEmail() + " not found"));
    }
}
