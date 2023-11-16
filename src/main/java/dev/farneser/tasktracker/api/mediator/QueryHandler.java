package dev.farneser.tasktracker.api.mediator;

public interface QueryHandler<REQUEST extends Query<RESPONSE>, RESPONSE> {
    RESPONSE handle(REQUEST query);
}