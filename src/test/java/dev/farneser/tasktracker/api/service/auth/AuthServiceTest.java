package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.service.ConfirmEmailService;
import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.DisabledException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private ConfirmEmailService confirmEmailService;

    @Test
    void register_SuccessfulRegistration_ThrowDisabledException() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("test1_register_service_com");
        registerDto.setEmail("test1@register-service.com");
        registerDto.setPassword("password");
        registerDto.setConfirmPassword("password");

        doNothing().when(confirmEmailService).sendRegisterMessage(anyString());

        assertThrows(DisabledException.class, () -> authService.register(registerDto));

        verify(confirmEmailService, times(1)).sendRegisterMessage(registerDto.getEmail());
    }

    @Test
    void register_InvalidUsername_ThrowsValidationException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("inv@lid usern@me");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("validpassword");
        registerDto.setConfirmPassword("validpassword");

        assertThrows(ValidationException.class, () -> authService.register(registerDto));
    }

    @Test
    void register_InvalidEmailFormat_ThrowsValidationException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("validusername");
        registerDto.setEmail("invalid-email");
        registerDto.setPassword("validpassword");
        registerDto.setConfirmPassword("validpassword");

        assertThrows(ValidationException.class, () -> authService.register(registerDto));
    }

    @Test
    void register_PasswordMismatch_ThrowsValidationException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("validusername");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password1");
        registerDto.setConfirmPassword("password2");

        assertThrows(ValidationException.class, () -> authService.register(registerDto));
    }

    @Test
    void register_PasswordTooShort_ThrowsValidationException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("validusername");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("pass");
        registerDto.setConfirmPassword("pass");

        assertThrows(ValidationException.class, () -> authService.register(registerDto));
    }

    @Test
    void register_PasswordTooLong_ThrowsValidationException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("validusername");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("verylongpasswordthatexceedsmaximumallowedlengthof64characters1234");
        registerDto.setConfirmPassword("verylongpasswordthatexceedsmaximumallowedlengthof64characters");

        assertThrows(ValidationException.class, () -> authService.register(registerDto));
    }

    @Test
    void register_NullUsername_ThrowsValidationException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("validpassword");
        registerDto.setConfirmPassword("validpassword");

        assertThrows(ValidationException.class, () -> authService.register(registerDto));
    }
}

