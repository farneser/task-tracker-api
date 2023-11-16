package dev.farneser.tasktracker.api.mediator;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;

public interface Mediator {
    <REQUEST extends Command<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request) throws NotFoundException;

    <REQUEST extends Query<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request) throws NotFoundException;
}
