package dev.farneser.tasktracker.api.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "project_name")
    private String projectName;
    @OneToMany(mappedBy = "project")
    private List<Status> statuses;
    @OneToMany(mappedBy = "project")
    private List<ProjectMember> members;
}
