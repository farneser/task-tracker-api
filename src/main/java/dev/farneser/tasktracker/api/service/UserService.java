package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.dto.UserDto;
import dev.farneser.tasktracker.api.operations.queries.user.getbyemail.GetUserByEmailQuery;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    private UserDto getByEmail(String email) throws NotFoundException {
        return mediator.send(new GetUserByEmailQuery(email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public UserDto getUser(Authentication authentication) throws NotFoundException {
        var username = authentication.getName();

        var user = getByEmail(username);

        return modelMapper.map(user, UserDto.class);
    }
}
