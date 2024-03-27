package dev.farneser.tasktracker.api.models.project;

import dev.farneser.tasktracker.api.models.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
