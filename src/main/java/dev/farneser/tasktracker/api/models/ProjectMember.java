package dev.farneser.tasktracker.api.models;

import dev.farneser.tasktracker.api.models.permissions.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "project_members")
public class ProjectMember {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private User member;
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
