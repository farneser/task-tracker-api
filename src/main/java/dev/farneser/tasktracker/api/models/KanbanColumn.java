package dev.farneser.tasktracker.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "columns")
public class KanbanColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "is_completed")
    private Boolean is_completed;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}