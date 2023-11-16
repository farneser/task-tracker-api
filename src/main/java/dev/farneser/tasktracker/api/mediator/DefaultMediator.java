package dev.farneser.tasktracker.api.mediator;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultMediator implements Mediator {
    private final ApplicationContext applicationContext;

    private static String convertFirstLetterToLowerCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        char[] chars = input.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        return new String(chars);
    }

    @Override
    public <REQUEST extends Command<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request) throws NotFoundException {
        if (request != null) {
            var handler = applicationContext.getBean(convertFirstLetterToLowerCase(request.getClass().getSimpleName()) + "Handler", CommandHandler.class);

            return (RESPONSE) handler.handle(request);
        } else {
            throw new UnsupportedOperationException("Unsupported request type");
        }
    }

    @Override
    public <REQUEST extends Query<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request) throws NotFoundException {
        if (request != null) {
            var handler = applicationContext.getBean(convertFirstLetterToLowerCase(request.getClass().getSimpleName()) + "Handler", QueryHandler.class);
            return (RESPONSE) handler.handle(request);
        } else {
            throw new UnsupportedOperationException("Unsupported request type");
        }
    }
}