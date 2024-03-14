package dev.farneser.tasktracker.api.models;

import dev.farneser.tasktracker.api.service.order.OrderIdentifier;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "columns")
public class KanbanColumn implements OrderIdentifier {

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "edit_date", nullable = false)
    private Date editDate;
}