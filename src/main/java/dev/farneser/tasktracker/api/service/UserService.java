package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.user.patch.PatchUserCommand;
import dev.farneser.tasktracker.api.operations.queries.user.getbyemail.GetUserByEmailQuery;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.UserRepository;
import dev.farneser.tasktracker.api.web.dto.user.PatchUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The `UserService` class provides functionality related to user management.
 * It implements the Spring `UserDetailsService` interface for user authentication.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Mediator mediator;
    private final ModelMapper modelMapper;

    /**
     * Load user details by username (email) for authentication.
     *
     * @param email The email (username) of the user.
     * @return UserDetails representing the user.
     * @throws UsernameNotFoundException If the user with the given email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    /**
     * Get user details for the authenticated user.
     *
     * @param authentication The authentication object representing the current user.
     * @return UserView representing the authenticated user.
     * @throws NotFoundException If the user is not found.
     */
    public UserView getUser(Authentication authentication) throws NotFoundException {
        var username = authentication.getName();

        log.debug("Getting user {}", username);

        return mediator.send(new GetUserByEmailQuery(username));
    }

    /**
     * Patch user details for the authenticated user.
     *
     * @param patchUserDto   The data to patch the user.
     * @param authentication The authentication object representing the current user.
     * @return UserView representing the patched user.
     * @throws NotFoundException If the user is not found.
     */
    public UserView patch(PatchUserDto patchUserDto, Authentication authentication) throws NotFoundException {
        log.debug("Patching user {} with {}", authentication.getName(), patchUserDto);

        var user = getUser(authentication);

        log.debug("User {} found", user.getId());

        var command = modelMapper.map(patchUserDto, PatchUserCommand.class);

        command.setUserId(user.getId());

        log.debug("Sending patch user command {}", command);

        mediator.send(command);

        log.debug("Patch user command sent");

        return getUser(authentication);
    }
}
