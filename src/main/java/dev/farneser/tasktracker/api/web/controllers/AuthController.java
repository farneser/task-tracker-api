package dev.farneser.tasktracker.api.web.controllers;

import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.service.auth.AuthService;
import dev.farneser.tasktracker.api.web.dto.auth.JwtDto;
import dev.farneser.tasktracker.api.web.dto.auth.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import dev.farneser.tasktracker.api.web.models.Message;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @ApiOperation(value = "Authenticate", notes = "Authenticates a user")
    public ResponseEntity<JwtDto> authenticate(@RequestBody @Valid LoginRequest loginDto) throws NotFoundException {
        return ResponseEntity.ok(authService.authenticate(loginDto));
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered user"),
            @ApiResponse(responseCode = "409", description = "Failed to register user")
    })
    @ApiOperation(value = "Register", notes = "Registers a user")
    public ResponseEntity<JwtDto> register(@RequestBody @Valid RegisterDto registerDto) throws InternalServerException, UniqueDataException {
        return ResponseEntity.ok(authService.register(registerDto));
    }

    @PostMapping("/refresh")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed auth token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @ApiOperation(value = "Refresh auth token", notes = "Refreshes the auth token for a user")
    public ResponseEntity<JwtDto> refresh(@RequestBody @Valid JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException, NotFoundException {
        return ResponseEntity.ok(authService.refresh(jwtDto));
    }

    @PostMapping("/confirm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully confirmed email"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @ApiOperation(value = "Confirm user", notes = "Confirms a user email")
    public ResponseEntity<Message> confirm(@RequestParam UUID token) throws NotFoundException {
        authService.activateAccount(token);

        return ResponseEntity.ok(Message.body("Successfully confirmed email"));
    }
}
