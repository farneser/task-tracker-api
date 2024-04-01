package dev.farneser.tasktracker.api.mediator;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;

public interface Mediator {
    <REQUEST extends Command<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request) throws NotFoundException, OperationNotAuthorizedException;

    <REQUEST extends Query<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request) throws NotFoundException, OperationNotAuthorizedException;
}
