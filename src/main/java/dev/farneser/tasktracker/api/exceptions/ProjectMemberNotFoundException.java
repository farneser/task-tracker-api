package dev.farneser.tasktracker.api.exceptions;

public class ProjectMemberNotFoundException extends NotFoundException {
    public ProjectMemberNotFoundException(Long userId) {
        super("Project member with user id: " + userId + " not found");
    }
}
