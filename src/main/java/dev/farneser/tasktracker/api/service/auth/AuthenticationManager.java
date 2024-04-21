package dev.farneser.tasktracker.api.service.auth;

public interface AuthenticationManager {
    void auth(String username, String password);
}