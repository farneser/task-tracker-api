package dev.farneser.tasktracker.api.web.controllers;

import dev.farneser.tasktracker.api.dto.Message;
import dev.farneser.tasktracker.api.dto.auth.AuthToken;
import dev.farneser.tasktracker.api.dto.auth.LoginRequest;
import dev.farneser.tasktracker.api.dto.auth.RegisterRequest;
import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.models.JwtStack;
import dev.farneser.tasktracker.api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = EndpointConstants.AUTH_ENDPOINT)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    private static void setRefreshTokenCookie(HttpServletResponse response, JwtStack jwtStack) {
        Cookie cookie = new Cookie("refreshToken", jwtStack.getRefreshToken());

        cookie.setMaxAge((int) (jwtStack.getRefreshTokenExpiration() / 1000));
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    private static String getRefreshTokenCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    @PostMapping
    @Operation(summary = "Authenticate user", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated user"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "401", description = "Bad credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AuthToken> authenticate(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        log.info("Authenticating user {}", loginRequest.getLogin());

        JwtStack jwtStack = authService.authenticate(this::authenticate, loginRequest);

        setRefreshTokenCookie(response, jwtStack);

        return ResponseEntity.ok(new AuthToken(jwtStack.getAccessToken()));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Register user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered user"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<AuthToken> register(
            @RequestBody @Valid RegisterRequest registerRequest,
            HttpServletResponse response
    ) throws InternalServerException, UniqueDataException, NotFoundException, ValidationException, OperationNotAuthorizedException {
        log.info("Registering user {}", registerRequest.getEmail());

        JwtStack jwtStack = authService.register(this::authenticate, registerRequest);

        setRefreshTokenCookie(response, jwtStack);

        return ResponseEntity.ok(new AuthToken(jwtStack.getAccessToken()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "Refresh JWT token and return new JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed JWT token"),
            @ApiResponse(responseCode = "400", description = "Invalid JWT token"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AuthToken> refresh(HttpServletResponse response, HttpServletRequest request)
            throws TokenExpiredException, InvalidTokenException, NotFoundException, OperationNotAuthorizedException, ValidationException {

        JwtStack jwtStack = authService.refresh(getRefreshTokenCookie(request));

        setRefreshTokenCookie(response, jwtStack);

        return ResponseEntity.ok(new AuthToken(jwtStack.getAccessToken()));
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirm email", description = "Confirm email and activate account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully confirmed email"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Message> confirm(
            @Parameter(description = "Confirm email token (uuid)")
            @RequestParam UUID token
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.info("Confirming email with token {}", token);

        authService.activateAccount(token);

        return ResponseEntity.ok(Message.body("Successfully confirmed email"));
    }
}
