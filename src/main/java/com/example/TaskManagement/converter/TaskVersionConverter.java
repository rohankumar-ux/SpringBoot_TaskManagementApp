package com.example.TaskManagement.converter;

import com.example.TaskManagement.dto.TaskVersionResponseDto;
import com.example.TaskManagement.model.TaskVersion;


public class TaskVersionConverter {
    
    public static TaskVersionResponseDto toTaskVersionResponseDto(TaskVersion version){
        return new TaskVersionResponseDto(
                version.getId(),
                version.getVersion(),
                version.getTitle(),
                version.getDescription(),
                version.getStatus(),
                version.getPriority(),
                version.getCreatedBy(),
                version.getAssignedTo(),
                version.getDueDate(),
                version.getTags(),
                version.getVersionedAt(),
                version.getChangeSummary()
        );
    }
}
