package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.dto.auth.LoginRequest;
import dev.farneser.tasktracker.api.dto.auth.RegisterRequest;
import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.models.JwtStack;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.tokens.RefreshToken;
import dev.farneser.tasktracker.api.operations.commands.refreshtoken.create.CreateRefreshTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid.GetRefreshTokenByTokenQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbylogin.GetUserByLoginQuery;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.service.ConfirmEmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final JwtService jwtService;
    private final ConfirmEmailService confirmEmailService;
    private final Mediator mediator;
    private final ModelMapper modelMapper;

    /**
     * Registers a new user with the provided registration data.
     *
     * @param registerRequest The registration data for the new user.
     * @return A JWT containing an access token and a refresh token upon successful registration.
     * @throws InternalServerException If an unexpected internal server error occurs.
     * @throws UniqueDataException     If the provided email is already taken.
     */
    public JwtStack register(AuthenticationManager authenticationManager, @Valid RegisterRequest registerRequest) throws InternalServerException, UniqueDataException, ValidationException, NotFoundException, OperationNotAuthorizedException {
        log.debug("Registering user {}", registerRequest.getEmail());

        try {
            RegisterUserCommand command = modelMapper.map(registerRequest, RegisterUserCommand.class);

            log.debug("Registering user {}", registerRequest.getEmail());

            command.setSubscribed(false);

            mediator.send(command);

            log.debug("Registering user {}", registerRequest.getEmail());

            confirmEmailService.sendRegisterMessage(registerRequest.getEmail());

            log.debug("Registering user {}", registerRequest.getEmail());

            return authenticate(authenticationManager, new LoginRequest(registerRequest.getEmail(), registerRequest.getPassword()));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueDataException(registerRequest.getEmail() + " already taken");
        }
    }

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param loginRequest The login credentials.
     * @return A JWT containing an access token and a refresh token upon successful authentication.
     * @throws BadCredentialsException If the provided credentials are invalid.
     */
    public JwtStack authenticate(AuthenticationManager authenticationManager, LoginRequest loginRequest) {
        try {
            UserView user = mediator.send(new GetUserByLoginQuery(loginRequest.getLogin()));

            log.debug("Authenticating user {}", loginRequest.getLogin());

            if (!user.isEnabled()) {
                log.debug("User {} is not enabled", loginRequest.getLogin());

                confirmEmailService.requireConfirm(user.getEmail());
            }

            log.debug("Authenticating user {}", loginRequest.getLogin());

            if (authenticationManager != null) {
                authenticationManager.auth(user.getUsername(), loginRequest.getPassword());
            }

            log.debug("Authenticating user {}", loginRequest.getLogin());

            return new JwtStack(jwtService.generateAccessToken(user.getUsername()), updateRefreshToken(user.getUsername()), jwtService.getAccessTokenExpiration(), jwtService.getRefreshTokenExpiration());
        } catch (BadCredentialsException | UsernameNotFoundException | NotFoundException |
                 OperationNotAuthorizedException | ValidationException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param refreshToken The refresh token.
     * @return A new JWT with an updated access token and refresh token.
     * @throws TokenExpiredException If the refresh token is expired.
     * @throws InvalidTokenException If the refresh token is invalid.
     * @throws NotFoundException     If the refresh token is not found.
     */
    public JwtStack refresh(String refreshToken) throws TokenExpiredException, InvalidTokenException, NotFoundException,
            OperationNotAuthorizedException, ValidationException {
        log.debug("Refreshing token {}", refreshToken);

        RefreshToken refreshTokenDto = mediator.send(new GetRefreshTokenByTokenQuery(refreshToken));

        log.debug("Refreshing token {}", refreshToken);

        User user = refreshTokenDto.getUser();

        log.debug("User {} found", user.getEmail());

        if (!jwtService.isRefreshTokenValid(refreshToken, user.getEmail())) {
            log.debug("Token {} is invalid", refreshToken);

            throw new TokenExpiredException("Invalid token");
        }
        log.debug("Refreshing token for user {}", user.getEmail());

        return new JwtStack(jwtService.generateAccessToken(user.getUsername()), updateRefreshToken(user.getUsername()), jwtService.getAccessTokenExpiration(), jwtService.getRefreshTokenExpiration());
    }

    /**
     * Updates the refresh token for the specified user and returns the new refresh token.
     *
     * @param email The email of the user for whom to update the refresh token.
     * @return The newly generated refresh token.
     * @throws NotFoundException If the user is not found.
     */
    private String updateRefreshToken(String email) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.debug("Updating refresh token for user {}", email);

        UserView user = mediator.send(new GetUserByLoginQuery(email));

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
    public void activateAccount(UUID id) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.debug("Activating account {}", id);

        confirmEmailService.confirm(id);

        log.debug("Account {} activated", id);
    }
}
