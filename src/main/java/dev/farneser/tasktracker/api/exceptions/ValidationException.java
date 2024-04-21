package dev.farneser.tasktracker.api.exceptions;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
