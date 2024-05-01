package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.dto.auth.RegisterRequest;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.models.JwtStack;
import dev.farneser.tasktracker.api.service.ConfirmEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceRegisterTest {
    @Autowired
    private AuthService authService;

    @MockBean
    private ConfirmEmailService confirmEmailService;

    @Test
    void register_SuccessfulRegistration_ThrowDisabledException() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("test1_register_service_com");
        registerRequest.setEmail("test1@register-service.com");
        registerRequest.setPassword("password");
        registerRequest.setConfirmPassword("password");

        doNothing().when(confirmEmailService).sendRegisterMessage(anyString());

        JwtStack dto = authService.register(null, registerRequest);

        assertNotNull(dto);
        assertNotNull(dto.getAccessToken());
        assertNotNull(dto.getRefreshToken());

        verify(confirmEmailService, times(1)).sendRegisterMessage(registerRequest.getEmail());
    }

    @Test
    void register_InvalidUsername_ThrowsValidationException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("inv@lid usern@me");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("validpassword");
        registerRequest.setConfirmPassword("validpassword");

        assertThrows(ValidationException.class, () -> authService.register(null, registerRequest));
    }

    @Test
    void register_InvalidEmailFormat_ThrowsValidationException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validusername");
        registerRequest.setEmail("invalid-email");
        registerRequest.setPassword("validpassword");
        registerRequest.setConfirmPassword("validpassword");

        assertThrows(ValidationException.class, () -> authService.register(null, registerRequest));
    }

    @Test
    void register_PasswordMismatch_ThrowsValidationException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validusername");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password1");
        registerRequest.setConfirmPassword("password2");

        assertThrows(ValidationException.class, () -> authService.register(null, registerRequest));
    }

    @Test
    void register_PasswordTooShort_ThrowsValidationException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validusername");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("pass");
        registerRequest.setConfirmPassword("pass");

        assertThrows(ValidationException.class, () -> authService.register(null, registerRequest));
    }

    @Test
    void register_PasswordTooLong_ThrowsValidationException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("validusername");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("verylongpasswordthatexceedsmaximumallowedlengthof64characters1234");
        registerRequest.setConfirmPassword("verylongpasswordthatexceedsmaximumallowedlengthof64characters");

        assertThrows(ValidationException.class, () -> authService.register(null, registerRequest));
    }

    @Test
    void register_NullUsername_ThrowsValidationException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("validpassword");
        registerRequest.setConfirmPassword("validpassword");

        assertThrows(ValidationException.class, () -> authService.register(null, registerRequest));
    }
}


