package dev.farneser.tasktracker.api.repository;

import dev.farneser.tasktracker.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}