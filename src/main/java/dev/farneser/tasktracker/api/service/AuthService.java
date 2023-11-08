package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.UserRepository;
import dev.farneser.tasktracker.api.utils.JwtService;
import dev.farneser.tasktracker.api.web.dto.AuthResponse;
import dev.farneser.tasktracker.api.web.dto.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(@Valid RegisterRequest registerRequest) {
        try {
            var user = User
                    .builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getEmail()))
                    .build();

            user = userRepository.save(user);

            return new AuthResponse(jwtService.generateToken(user));

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with email: " + loginRequest.getEmail() + " not found"));

        return new AuthResponse(jwtService.generateToken(user));
    }
}
