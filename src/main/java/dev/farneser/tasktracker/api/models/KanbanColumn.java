package dev.farneser.tasktracker.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "columns")
public class KanbanColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "order_number")
    private Long orderNumber;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL)
    private List<KanbanTask> tasks;
}