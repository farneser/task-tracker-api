package dev.farneser.tasktracker.api.mediator;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;

public interface CommandHandler<REQUEST extends Command<RESPONSE>, RESPONSE> {
    RESPONSE handle(REQUEST command) throws NotFoundException, OperationNotAuthorizedException;
}