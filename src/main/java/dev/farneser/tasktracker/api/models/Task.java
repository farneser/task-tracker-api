package dev.farneser.tasktracker.api.models;

import dev.farneser.tasktracker.api.service.order.OrderIdentifier;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task implements OrderIdentifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedFor;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @Column(name = "order_number")
    private Long orderNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "edit_date", nullable = false)
    private Date editDate;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", assignedFor=" + (assignedFor != null ? assignedFor.getId() : "null") +
                ", status=" + (status != null ? status.getId() : "null") +
                ", orderNumber=" + orderNumber +
                ", creationDate='" + creationDate + '\'' +
                ", editDate='" + editDate + '\'' +
                '}';
    }
}
