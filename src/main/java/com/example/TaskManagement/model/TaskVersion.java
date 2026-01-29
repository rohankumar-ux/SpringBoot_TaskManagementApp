package com.example.TaskManagement.model;

import com.example.TaskManagement.enums.Priority;
import com.example.TaskManagement.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "task_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID taskId;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private UUID createdBy;

    private UUID assignedTo;

    private LocalDate dueDate;

    @ElementCollection
    @CollectionTable(name = "task_version_tags", joinColumns = @JoinColumn(name = "version_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime versionedAt;

    @Column(columnDefinition = "TEXT")
    private String changeSummary;

    @PrePersist
    protected void onCreate() {
        versionedAt = LocalDateTime.now();
    }
}
