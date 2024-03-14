package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.operations.commands.refreshtoken.create.CreateRefreshTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid.GetRefreshTokenByTokenQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyemail.GetUserByEmailQuery;
import dev.farneser.tasktracker.api.operations.views.UserView;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The `AuthService` class provides authentication and authorization-related functionality,
 * including user registration, login, token refresh, and account activation.
 * It interacts with the authentication manager, JWT service, confirmation email service, and mediator
 * to handle user-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ConfirmEmailService confirmEmailService;
    private final Mediator mediator;
    private final ModelMapper modelMapper;

    /**
     * Registers a new user with the provided registration data.
     *
     * @param registerDto The registration data for the new user.
     * @return A JWT containing an access token and a refresh token upon successful registration.
     * @throws InternalServerException If an unexpected internal server error occurs.
     * @throws UniqueDataException     If the provided email is already taken.
     */
    public JwtDto register(@Valid RegisterDto registerDto) throws InternalServerException, UniqueDataException {
        log.debug("Registering user {}", registerDto.getEmail());

        try {
            RegisterUserCommand command = modelMapper.map(registerDto, RegisterUserCommand.class);

            log.debug("Registering user {}", registerDto.getEmail());

            mediator.send(command);

            log.debug("Registering user {}", registerDto.getEmail());

            confirmEmailService.sendRegisterMessage(registerDto.getEmail());

            log.debug("Registering user {}", registerDto.getEmail());

            return authenticate(new LoginRequest(registerDto.getEmail(), registerDto.getPassword()));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueDataException(registerDto.getEmail() + " already taken");
        } catch (DisabledException | BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param loginRequest The login credentials.
     * @return A JWT containing an access token and a refresh token upon successful authentication.
     * @throws BadCredentialsException If the provided credentials are invalid.
     */
    public JwtDto authenticate(LoginRequest loginRequest) {
        try {
            UserView user = mediator.send(new GetUserByEmailQuery(loginRequest.getEmail()));

            log.debug("Authenticating user {}", loginRequest.getEmail());

            if (!user.isEnabled()) {
                log.debug("User {} is not enabled", loginRequest.getEmail());

                confirmEmailService.requireConfirm(user.getEmail());
            }

            log.debug("Authenticating user {}", loginRequest.getEmail());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            log.debug("Authenticating user {}", loginRequest.getEmail());

            return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
        } catch (BadCredentialsException | UsernameNotFoundException | NotFoundException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param jwtDto The JWT containing the refresh token.
     * @return A new JWT with an updated access token and refresh token.
     * @throws TokenExpiredException If the refresh token is expired.
     * @throws InvalidTokenException If the refresh token is invalid.
     * @throws NotFoundException     If the refresh token is not found.
     */
    public JwtDto refresh(JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException, NotFoundException {
        log.debug("Refreshing token {}", jwtDto.getRefreshToken());

        RefreshToken refreshToken = mediator.send(new GetRefreshTokenByTokenQuery(jwtDto.getRefreshToken()));

        log.debug("Refreshing token {}", jwtDto.getRefreshToken());

        User user = refreshToken.getUser();

        log.debug("User {} found", user.getEmail());

        if (!jwtService.isRefreshTokenValid(jwtDto.getRefreshToken(), user.getEmail())) {
            log.debug("Token {} is invalid", jwtDto.getRefreshToken());

            throw new TokenExpiredException("Invalid token");
        }
        log.debug("Refreshing token for user {}", user.getEmail());

        return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
    }

    /**
     * Updates the refresh token for the specified user and returns the new refresh token.
     *
     * @param email The email of the user for whom to update the refresh token.
     * @return The newly generated refresh token.
     * @throws NotFoundException If the user is not found.
     */
    private String updateRefreshToken(String email) throws NotFoundException {
        log.debug("Updating refresh token for user {}", email);

        UserView user = mediator.send(new GetUserByEmailQuery(email));

        log.debug("User {} found", user.getEmail());

        String tokenString = jwtService.generateRefreshToken(user.getEmail());

        log.debug("Token generated for user {}", user.getEmail());

        mediator.send(new CreateRefreshTokenCommand(tokenString, user.getId()));

        log.debug("Token saved for user {}", user.getEmail());

        return tokenString;
    }

    /**
     * Activates a user account with the specified activation ID.
     *
     * @param id The activation ID.
     * @throws NotFoundException If the activation ID is not found.
     */
    public void activateAccount(UUID id) throws NotFoundException {
        log.debug("Activating account {}", id);

        confirmEmailService.confirm(id);

        log.debug("Account {} activated", id);
    }
}
