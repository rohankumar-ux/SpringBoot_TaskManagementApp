package com.example.TaskManagement.dto;

import com.example.TaskManagement.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Created by user ID is required")
    private UUID createdBy;

    private UUID assignedTo;
    private Priority priority;
    private LocalDate dueDate;
    private List<String> tags;
}