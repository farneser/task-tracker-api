package dev.farneser.tasktracker.api.models.tokens;

import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.models.project.Project;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_invite_tokens")
public class ProjectInviteToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public ProjectInviteToken(User user, Project project) {
        this.user = user;
        this.project = project;

        token = UUID.randomUUID().toString();
    }
}
