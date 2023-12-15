package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.refreshtoken.create.CreateRefreshTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid.GetRefreshTokenByTokenQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyemail.GetUserByEmailQuery;
import dev.farneser.tasktracker.api.service.ConfirmEmailService;
import dev.farneser.tasktracker.api.web.dto.auth.JwtDto;
import dev.farneser.tasktracker.api.web.dto.auth.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ConfirmEmailService confirmEmailService;
    private final Mediator mediator;
    private final ModelMapper modelMapper;

    public JwtDto register(@Valid RegisterDto registerDto) throws InternalServerException, UniqueDataException {
        log.debug("Registering user {}", registerDto.getEmail());

        try {
            var command = modelMapper.map(registerDto, RegisterUserCommand.class);

            log.debug("Registering user {}", registerDto.getEmail());

            mediator.send(command);

            log.debug("Registering user {}", registerDto.getEmail());

            confirmEmailService.sendRegisterMessage(registerDto.getEmail());

            log.debug("Registering user {}", registerDto.getEmail());

            return authenticate(new LoginRequest(registerDto.getEmail(), registerDto.getPassword()));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueDataException(registerDto.getEmail() + " already taken");
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public JwtDto authenticate(LoginRequest loginRequest) throws UsernameNotFoundException, NotFoundException {
        var user = mediator.send(new GetUserByEmailQuery(loginRequest.getEmail()));

        log.debug("Authenticating user {}", loginRequest.getEmail());

        if (!user.isEnabled()) {
            log.debug("User {} is not enabled", loginRequest.getEmail());

            confirmEmailService.requireConfirm(user.getEmail());
        }

        log.debug("Authenticating user {}", loginRequest.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        log.debug("Authenticating user {}", loginRequest.getEmail());

        return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
    }

    public JwtDto refresh(JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException, NotFoundException {
        log.debug("Refreshing token {}", jwtDto.getRefreshToken());

        var refreshToken = mediator.send(new GetRefreshTokenByTokenQuery(jwtDto.getRefreshToken()));

        log.debug("Refreshing token {}", jwtDto.getRefreshToken());

        var user = refreshToken.getUser();

        log.debug("User {} found", user.getEmail());

        if (!jwtService.isRefreshTokenValid(jwtDto.getRefreshToken(), user.getEmail())) {
            log.debug("Token {} is invalid", jwtDto.getRefreshToken());

            throw new TokenExpiredException("Invalid token");
        }
        log.debug("Refreshing token for user {}", user.getEmail());

        return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
    }

    private String updateRefreshToken(String email) throws NotFoundException {
        log.debug("Updating refresh token for user {}", email);

        var user = mediator.send(new GetUserByEmailQuery(email));

        log.debug("User {} found", user.getEmail());

        var tokenString = jwtService.generateRefreshToken(user.getEmail());

        log.debug("Token generated for user {}", user.getEmail());

        mediator.send(new CreateRefreshTokenCommand(tokenString, user.getId()));

        log.debug("Token saved for user {}", user.getEmail());

        return tokenString;
    }

    public void activateAccount(UUID id) throws NotFoundException {
        log.debug("Activating account {}", id);

        confirmEmailService.confirm(id);

        log.debug("Account {} activated", id);
    }
}
