package dev.farneser.tasktracker.api.web.miscellaneous;

import dev.farneser.tasktracker.api.dto.Message;
import dev.farneser.tasktracker.api.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Message> handler(BadCredentialsException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Message> handler(InvalidTokenException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Message> handler(TokenExpiredException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Message> handler(ExpiredJwtException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<Message> handler(HttpMessageNotReadableException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> handler(MethodArgumentNotValidException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.BAD_REQUEST, ex.getFieldError() != null
                ? ex.getFieldError().getDefaultMessage()
                : ex.getBindingResult().getGlobalErrors().get(0).getDefaultMessage()
        );
    }

    @ExceptionHandler(UniqueDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Message> handler(UniqueDataException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Message> handler(NotFoundException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Message> handler(UsernameNotFoundException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Message> handler(DisabledException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Message> handler(AccessDeniedException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(OperationNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Message> handler(OperationNotAuthorizedException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> handler(ValidationException ex) {
        log.debug(ex.getMessage());

        return getResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Message> handler(Exception ex) {
        log.debug(ex.getClass().getSimpleName() + " " + ex.getMessage());

        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<Message> getResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Message
                .builder()
                .message(message)
                .status(status.value())
                .build()
        );
    }
}
