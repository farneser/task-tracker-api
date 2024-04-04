package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userSaveTest() {
        User user = new User();

        user.setUsername("test");
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setRegisterDate(new Date(System.currentTimeMillis()));

        userRepository.save(user);

        assert userRepository.findByEmail(user.getEmail()).orElseThrow().getId() != null;
    }
}
