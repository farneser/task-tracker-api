package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.InternalServerException;
import dev.farneser.tasktracker.api.exceptions.InvalidTokenException;
import dev.farneser.tasktracker.api.exceptions.TokenExpiredException;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import dev.farneser.tasktracker.api.web.dto.JwtDto;
import dev.farneser.tasktracker.api.web.dto.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtDto register(@Valid RegisterRequest registerRequest) throws InternalServerException {
        try {
            var user = User
                    .builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .registerDate(new Date(System.currentTimeMillis()))
                    .build();

            user = userRepository.save(user);

            return new JwtDto(jwtService.generateAccessToken(user), this.updateRefreshToken(user));

        } catch (Exception e) {

            throw new InternalServerException(e.getMessage());
        }
    }

    public JwtDto authenticate(LoginRequest loginRequest) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with email: " + loginRequest.getEmail() + " not found"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        return new JwtDto(jwtService.generateAccessToken(user), updateRefreshToken(user));
    }

    public JwtDto refresh(JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException {
        var userName = jwtService.extractUsername(jwtDto.getRefreshToken());

        var user = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("User with email: " + userName + " not found"));

        if (!jwtService.isRefreshTokenValid(jwtDto.getRefreshToken(), user)) {
            throw new TokenExpiredException("Invalid token");
        }

        return new JwtDto(jwtService.generateAccessToken(user), updateRefreshToken(user));
    }

    private String updateRefreshToken(User user) {

        var token = tokenRepository.findByUser(user);

        // deletion if available
        token.ifPresent(tokenRepository::delete);

        var newToken = RefreshToken.builder().token(jwtService.generateRefreshToken(user)).user(user).build();

        return newToken.getToken();
    }
}
