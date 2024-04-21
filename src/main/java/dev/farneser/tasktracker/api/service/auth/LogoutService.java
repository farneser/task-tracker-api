package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.refreshtoken.delete.DeleteRefreshTokenCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * The `LogoutService` class provides logout functionality by handling the logout process.
 * It implements the Spring Security `LogoutHandler` interface and deletes the associated refresh token
 * when a user logs out.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final Mediator mediator;

    /**
     * Handles the logout process by deleting the associated refresh token and clearing the security context.
     *
     * @param request        The HTTP request.
     * @param response       The HTTP response.
     * @param authentication The authentication object representing the user.
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        log.debug("Logging out user {}", authentication.getName());

        String authHeader = request.getHeader("Authorization");

        log.debug("Auth header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        log.debug("Auth header: {}", authHeader);

        String jwt = authHeader.substring(7);

        try {
            log.debug("Deleting refresh token {}", jwt);

            mediator.send(new DeleteRefreshTokenCommand(jwt));
        } catch (NotFoundException | OperationNotAuthorizedException | ValidationException e) {
            log.debug(e.getMessage());
        }

        log.debug("Logging out user {}", authentication.getName());

        SecurityContextHolder.clearContext();
    }
}