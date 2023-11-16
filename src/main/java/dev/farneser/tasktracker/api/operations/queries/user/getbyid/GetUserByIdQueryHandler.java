package dev.farneser.tasktracker.api.operations.queries.user.getbyid;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserByIdQueryHandler implements QueryHandler<GetUserByIdQuery, User> {
    private final UserRepository userRepository;

    @Override
    public User handle(GetUserByIdQuery query) throws NotFoundException {
        return userRepository.findById(query.getId()).orElseThrow(() -> new NotFoundException("User with id: " + query.getId() + " not found"));
    }
}
