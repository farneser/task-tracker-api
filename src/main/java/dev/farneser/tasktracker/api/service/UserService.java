package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.user.patch.PatchUserCommand;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.repository.UserRepository;
import dev.farneser.tasktracker.api.web.dto.user.PatchUserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends BaseService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(Mediator mediator, ModelMapper modelMapper, UserRepository userRepository) {
        super(mediator, modelMapper);
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Loading user by email {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public UserView getUser(Authentication authentication) throws NotFoundException {
        return super.getUser(authentication);
    }

    public UserView patch(PatchUserDto patchUserDto, Authentication authentication) throws NotFoundException {
        log.debug("Patching user {} with {}", authentication.getName(), patchUserDto);

        var user = super.getUser(authentication);
        log.debug("User {} found", user.getId());

        var command = modelMapper.map(patchUserDto, PatchUserCommand.class);

        command.setUserId(user.getId());

        log.debug("Sending patch user command {}", command);

        mediator.send(command);

        log.debug("Patch user command sent");

        return getUser(authentication);
    }
}
