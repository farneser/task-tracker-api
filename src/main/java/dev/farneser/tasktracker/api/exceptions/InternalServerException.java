package dev.farneser.tasktracker.api.exceptions;

public class InternalServerException extends Exception {
    public InternalServerException(String message){
        super(message);
    }
}
