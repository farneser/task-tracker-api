package dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.accept;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AcceptProjectInviteTokenCommandTest {

    @Test
    void getUserId() {
        Long userId = 123L;
        AcceptProjectInviteTokenCommand command = new AcceptProjectInviteTokenCommand(userId, "token");

        Long retrievedUserId = command.getUserId();

        assertEquals(userId, retrievedUserId);
    }

    @Test
    void getToken() {
        String token = "test_token";
        AcceptProjectInviteTokenCommand command = new AcceptProjectInviteTokenCommand(456L, token);

        String retrievedToken = command.getToken();

        assertEquals(token, retrievedToken);
    }

    @Test
    void setUserId() {
        Long userId = 789L;
        AcceptProjectInviteTokenCommand command = new AcceptProjectInviteTokenCommand();

        command.setUserId(userId);

        assertEquals(userId, command.getUserId());
    }

    @Test
    void setToken() {
        String token = "new_token";
        AcceptProjectInviteTokenCommand command = new AcceptProjectInviteTokenCommand();

        command.setToken(token);

        assertEquals(token, command.getToken());
    }
}
