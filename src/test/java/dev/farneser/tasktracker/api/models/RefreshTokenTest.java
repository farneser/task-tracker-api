package dev.farneser.tasktracker.api.models;

import dev.farneser.tasktracker.api.models.tokens.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RefreshTokenTest {

    @Test
    public void testNoArgsConstructor() {
        RefreshToken refreshToken = new RefreshToken();
        assertNotNull(refreshToken);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String token = "test_token";
        User user = new User();

        RefreshToken refreshToken = new RefreshToken(id, token, user);

        assertEquals(id, refreshToken.getId());
        assertEquals(token, refreshToken.getToken());
        assertEquals(user, refreshToken.getUser());
    }

    @Test
    public void testIdGetter() {
        Long id = 1L;
        RefreshToken refreshToken = new RefreshToken(1L, null, null);
        assertEquals(id, refreshToken.getId());
    }

    @Test
    public void testTokenGetter() {
        String token = "test_token";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        assertEquals(token, refreshToken.getToken());
    }

    @Test
    public void testUserGetter() {
        User user = new User();
        RefreshToken refreshToken = new RefreshToken(1L, null, user);
        assertEquals(user, refreshToken.getUser());
    }
}
