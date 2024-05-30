package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.DatabaseInitializationExtension;
import dev.farneser.tasktracker.api.dto.user.PatchUserDto;
import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.service.auth.CustomUserAuthentication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(DatabaseInitializationExtension.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @ParameterizedTest
    @CsvSource({"user1", "user2@builder.com"})
    void getUser_Valid_ReturnUserView(String login)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(new CustomUserAuthentication(login));

        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getEmail());
        assertNotNull(user.getUsername());
        assertNotNull(user.getIsSubscribed());
        assertNotNull(user.getRegistrationDate());
    }

    @ParameterizedTest
    @CsvSource({"user 1", "user5@builder.com", "user2@builder.comm"})
    void getUser_Invalid_ReturnUserView(String login) {
        assertThrows(NotFoundException.class, () -> userService.getUser(new CustomUserAuthentication(login)));
    }

    @ParameterizedTest
    @CsvSource({"user1, true", "user2@builder.com, true", "user3, false",})
    void patchUser_Valid_ReturnUserView(String username, boolean value)
            throws ValidationException, NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.patch(new PatchUserDto(value), new CustomUserAuthentication(username));

        assertNotNull(user);
        assertEquals(user.getIsSubscribed(), value);
    }
}
