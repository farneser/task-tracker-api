package dev.farneser.tasktracker.api.web.controllers;

import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.service.AuthService;
import dev.farneser.tasktracker.api.web.dto.auth.JwtDto;
import dev.farneser.tasktracker.api.web.dto.auth.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<JwtDto> authenticate(@RequestBody @Valid LoginRequest loginDto) throws NotFoundException {
        return ResponseEntity.ok(authService.authenticate(loginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtDto> register(@RequestBody @Valid RegisterDto registerDto) throws InternalServerException, UniqueDataException {
        return ResponseEntity.ok(authService.register(registerDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody @Valid JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException, NotFoundException {
        return ResponseEntity.ok(authService.refresh(jwtDto));
    }
}
