package dev.farneser.tasktracker.api.mediator;

public interface Mediator {
    <REQUEST extends Command<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request);

    <REQUEST extends Query<RESPONSE>, RESPONSE> RESPONSE send(REQUEST request);
}
