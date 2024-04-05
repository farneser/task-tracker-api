package dev.farneser.tasktracker.api.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConfirmEmailTokenTest {

    @Test
    public void testConstructorAndGetters() {
        String email = "test@example.com";
        Date expiresAt = new Date();

        ConfirmEmailToken confirmEmailToken = new ConfirmEmailToken(email, expiresAt);

        assertNotNull(confirmEmailToken.getToken());
        assertEquals(email, confirmEmailToken.getEmail());
        assertEquals(expiresAt, confirmEmailToken.getExpiresAt());
    }

    @Test
    public void testTokenUniqueness() {
        String email1 = "test1@example.com";
        String email2 = "test2@example.com";
        Date expiresAt = new Date();

        ConfirmEmailToken confirmEmailToken1 = new ConfirmEmailToken(email1, expiresAt);
        ConfirmEmailToken confirmEmailToken2 = new ConfirmEmailToken(email2, expiresAt);

        assertNotEquals(confirmEmailToken1.getToken(), confirmEmailToken2.getToken());
    }
}
