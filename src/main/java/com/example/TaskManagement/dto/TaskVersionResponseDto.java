package com.example.TaskManagement.dto;

import com.example.TaskManagement.enums.Priority;
import com.example.TaskManagement.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVersionResponseDto {
    private UUID id;
    private Integer version;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private UUID createdBy;
    private UUID assignedTo;
    private LocalDate dueDate;
    private List<String> tags;
    private LocalDateTime versionedAt;
    private String changeSummary;
}