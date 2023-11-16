package dev.farneser.tasktracker.api.exceptions;

public class RefreshTokenNotFoundException extends NotFoundException {
    public RefreshTokenNotFoundException(String token) {
        super("Refresh token " + token + " not found");
    }

    public RefreshTokenNotFoundException(Long userId) {
        super("Refresh token with user id " + userId + " not found");
    }
}
