package dev.farneser.tasktracker.api.operations.queries.user.getbyemail;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.QueryHandler;
import dev.farneser.tasktracker.api.operations.dto.UserDto;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserByEmailQueryHandler implements QueryHandler<GetUserByEmailQuery, UserDto> {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto handle(GetUserByEmailQuery query) throws NotFoundException {
        var user = userRepository.findByEmail(query.getEmail()).orElseThrow(() -> new UserNotFoundException(query.getEmail()));

        return modelMapper.map(user, UserDto.class);

    }
}
