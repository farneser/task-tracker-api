package dev.farneser.tasktracker.api.exceptions;

public class ProjectNotFoundException extends NotFoundException {
    public ProjectNotFoundException(Long id) {
        super("Project with id: " + id + " not found");
    }
}
