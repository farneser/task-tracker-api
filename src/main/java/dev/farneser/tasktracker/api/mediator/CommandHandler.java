package dev.farneser.tasktracker.api.mediator;

public interface CommandHandler<REQUEST extends Command<RESPONSE>, RESPONSE> {
    RESPONSE handle(REQUEST command);
}