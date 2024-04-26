package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.DatabaseInitializationExtension;
import dev.farneser.tasktracker.api.dto.auth.JwtDto;
import dev.farneser.tasktracker.api.dto.auth.LoginRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(DatabaseInitializationExtension.class)
public class AuthServiceAuthenticateTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @ParameterizedTest
    @CsvSource({
            "user1@builder.com, password1",
            "user2, password2",
            "user3, password3",
    })
    void authenticate_ValidUsernameAndPassword_ReturnJwtDto(String username, String password) {
        LoginRequest login = new LoginRequest(username, password);

        JwtDto dto = authService.authenticate(this::authenticate, login);

        assertNotNull(dto);
        assertNotNull(dto.getAccessToken());
        assertNotNull(dto.getRefreshToken());
    }

    @ParameterizedTest
    @CsvSource({
            "user1@b uilder.com, password1",
            "user2, password3",
            "user2, password3",
            "u ser2, password2",
    })
    void authenticate_InvalidUsernameOrPassword_ReturnJwtDto(String username, String password) {
        LoginRequest login = new LoginRequest(username, password);

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(this::authenticate, login));
    }
}


