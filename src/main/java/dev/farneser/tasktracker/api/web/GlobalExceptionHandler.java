package dev.farneser.tasktracker.api.web;

import dev.farneser.tasktracker.api.exceptions.InvalidTokenException;
import dev.farneser.tasktracker.api.exceptions.TokenExpiredException;
import dev.farneser.tasktracker.api.models.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handler(BadCredentialsException ex) {
        log.error(ex.getMessage());
        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handler(InvalidTokenException ex) {
        log.error(ex.getMessage());
        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handler(TokenExpiredException ex) {
        log.error(ex.getMessage());
        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handler(ExpiredJwtException ex) {
        log.error(ex.getMessage());
        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ErrorResponse> handler(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());

        return getResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return getResponseEntity(HttpStatus.BAD_REQUEST, Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handler(UsernameNotFoundException ex) {
        log.error(ex.getMessage());
        return getResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handler(Exception ex) {
        log.error(ex.getMessage());
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> getResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(
                status).body(
                ErrorResponse
                        .builder()
                        .message(message)
                        .status(status.value())
                        .build());
    }
}
