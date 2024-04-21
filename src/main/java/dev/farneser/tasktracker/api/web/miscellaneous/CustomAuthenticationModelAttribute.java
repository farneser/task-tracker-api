package dev.farneser.tasktracker.api.web.miscellaneous;

import dev.farneser.tasktracker.api.service.auth.CustomUserAuthentication;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CustomAuthenticationModelAttribute {
    @ModelAttribute(AuthModel.AUTH_MODEL_NAME)
    public UserAuthentication customAuthentication(Authentication authentication) {
        if (authentication != null) {
            return new CustomUserAuthentication(authentication.getName());
        } else {
            return null;
        }
    }
}