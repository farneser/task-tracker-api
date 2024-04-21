package dev.farneser.tasktracker.api.mediator;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultMediator implements Mediator {
    private final ApplicationContext applicationContext;
    private final Validator validator;

    private static String convertFirstLetterToLowerCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        char[] chars = input.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        return new String(chars);
    }

    @Override
    public <REQUEST extends Command<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.debug("Send request: {}", request);

        if (request != null) {
            CommandHandler handler = applicationContext.getBean(
                    convertFirstLetterToLowerCase(request.getClass().getSimpleName()) + "Handler",
                    CommandHandler.class
            );

            log.debug(
                    "Handler for request: {}, {} found. Request data: {}",
                    request.getClass().getSimpleName(),
                    handler.getClass().getSimpleName(),
                    request
            );

            Set<ConstraintViolation<REQUEST>> violations = validator.validate(request);

            if (!violations.isEmpty()) {
                throw new ValidationException(violations.stream().map(ConstraintViolation::getMessage).toList());
            }

            return (RESPONSE) handler.handle(request);
        } else {
            log.debug("Unsupported request type");

            throw new UnsupportedOperationException("Unsupported request type");
        }
    }

    @Override
    public <REQUEST extends Query<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request)
            throws NotFoundException, OperationNotAuthorizedException {
        log.debug("Send request: {}", request);

        if (request != null) {
            QueryHandler handler = applicationContext.getBean(convertFirstLetterToLowerCase(
                            request.getClass().getSimpleName()) + "Handler",
                    QueryHandler.class
            );

            log.debug(
                    "Handler for request: {}, {} found. Request data: {}",
                    request.getClass().getSimpleName(),
                    handler.getClass().getSimpleName(),
                    request
            );

            return (RESPONSE) handler.handle(request);
        } else {
            log.debug("Unsupported request type");

            throw new UnsupportedOperationException("Unsupported request type");
        }
    }
}