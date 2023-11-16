package dev.farneser.tasktracker.api.mediator;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;

public interface QueryHandler<REQUEST extends Query<RESPONSE>, RESPONSE> {
    RESPONSE handle(REQUEST query) throws NotFoundException;
}