package dev.farneser.tasktracker.api.web.controllers;

import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.service.auth.AuthService;
import dev.farneser.tasktracker.api.web.dto.auth.JwtDto;
import dev.farneser.tasktracker.api.web.dto.auth.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import dev.farneser.tasktracker.api.web.models.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping
    @Operation(summary = "Authenticate user", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated user"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "401", description = "Bad credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<JwtDto> authenticate(@RequestBody @Valid LoginRequest loginDto) {
        log.info("Authenticating user {}", loginDto.getLogin());

        return ResponseEntity.ok(authService.authenticate(this::authenticate, loginDto));
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
    public ResponseEntity<JwtDto> register(@RequestBody @Valid RegisterDto registerDto)
            throws InternalServerException, UniqueDataException, NotFoundException, ValidationException, OperationNotAuthorizedException {
        log.info("Registering user {}", registerDto.getEmail());

        return ResponseEntity.status(201).body(authService.register(this::authenticate, registerDto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "Refresh JWT token and return new JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed JWT token"),
            @ApiResponse(responseCode = "400", description = "Invalid JWT token"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<JwtDto> refresh(@RequestBody @Valid JwtDto jwtDto)
            throws TokenExpiredException, InvalidTokenException, NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.info("Refreshing JWT token {}", jwtDto);

        return ResponseEntity.ok(authService.refresh(jwtDto));
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
