package com.example.TaskManagement.converter;


import com.example.TaskManagement.dto.TaskResponseDto;
import com.example.TaskManagement.dto.TaskSummaryDto;
import com.example.TaskManagement.model.Task;

import java.util.stream.Collectors;

public class TaskConverter {

    public static TaskResponseDto toTaskRespone(Task task){
        return new TaskResponseDto(
                task.getId() ,
                task.getVersion(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                UserConverter.toUserSummary(task.getCreatedBy()),
                UserConverter.toUserSummary(task.getAssignedTo()),
                task.getDueDate(),
                task.getTags(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getComments().stream()
                        .map(CommentConverter::toCommentResponse)
                        .collect(Collectors.toList())
        );
    }

    public static TaskSummaryDto toTaskSummary(Task task){
        return new TaskSummaryDto(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getPriority(),
                UserConverter.toUserSummary(task.getAssignedTo()),
                task.getDueDate()

        );
    }

}
