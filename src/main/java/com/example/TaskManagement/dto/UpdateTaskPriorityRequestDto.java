package com.example.TaskManagement.dto;

import com.example.TaskManagement.enums.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskPriorityRequestDto {
    @NotNull(message = "Priority is required")
    private Priority priority;
}
