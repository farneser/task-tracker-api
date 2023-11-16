package dev.farneser.tasktracker.api.exceptions;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
    }

    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " not found");
    }
}
