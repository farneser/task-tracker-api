package dev.farneser.tasktracker.api.service.auth;

public record CustomUserAuthentication(String name) implements UserAuthentication {
    @Override
    public String getName() {
        return name;
    }
}
