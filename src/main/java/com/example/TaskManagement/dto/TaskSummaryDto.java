package com.example.TaskManagement.dto;

import com.example.TaskManagement.enums.Priority;
import com.example.TaskManagement.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummaryDto {
    private UUID id;
    private String title;
    private TaskStatus status;
    private Priority priority;
    private UserSummaryDto assignedTo;
    private LocalDate dueDate;
}
