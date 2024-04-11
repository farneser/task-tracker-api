package dev.farneser.tasktracker.api.exceptions;

public class OperationNotAuthorizedException extends Exception {
    public OperationNotAuthorizedException() {
    }

    public OperationNotAuthorizedException(String message) {
        super(message);
    }
}
