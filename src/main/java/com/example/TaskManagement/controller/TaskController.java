package com.example.TaskManagement.controller;


import com.example.TaskManagement.converter.TaskConverter;
import com.example.TaskManagement.dto.*;
import com.example.TaskManagement.enums.Priority;
import com.example.TaskManagement.enums.TaskStatus;
import com.example.TaskManagement.model.Task;
import com.example.TaskManagement.service.TaskSearchService;
import com.example.TaskManagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskSearchService taskSearchService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody CreateTaskRequestDto request) {
        TaskResponseDto response = taskService.createTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id) {
        TaskResponseDto response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskStatusRequestDto request,
            @RequestParam UUID performedBy) {
        TaskResponseDto response = taskService.updateTaskStatus(id, request, performedBy);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<TaskResponseDto> assignTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskAssigneeRequestDto request,
            @RequestParam UUID performedBy) {
        TaskResponseDto response = taskService.assignTask(id, request, performedBy);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<TaskResponseDto> updateTaskPriority(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskPriorityRequestDto request,
            @RequestParam UUID performedBy) {
        TaskResponseDto response = taskService.updateTaskPriority(id, request, performedBy);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/due-date")
    public ResponseEntity<TaskResponseDto> updateTaskDueDate(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskDueDateRequestDto request,
            @RequestParam UUID performedBy) {
        TaskResponseDto response = taskService.updateTaskDueDate(id, request, performedBy);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable UUID id,
            @Valid @RequestBody AddCommentRequestDto request) {
        CommentResponseDto response = taskService.addComment(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<TaskVersionResponseDto>> getTaskHistory(@PathVariable UUID id) {
        List<TaskVersionResponseDto> response = taskService.getTaskHistory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/by-status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<Task> tasks = taskSearchService.getAllTasksByStatus(status);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/by-priority/{priority}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByPriority(@PathVariable Priority priority) {
        List<Task> tasks = taskSearchService.getAllTasksByPriority(priority);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/overdue")
    public ResponseEntity<List<TaskResponseDto>> getOverdueTasks() {
        List<Task> tasks = taskSearchService.getAllOverdueTasks();
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/assigned-to/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksAssignedTo(@PathVariable UUID userId) {
        List<Task> tasks = taskSearchService.getAllTasksAssignedTo(userId);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/created-by/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksCreatedBy(@PathVariable UUID userId) {
        List<Task> tasks = taskSearchService.getAllTasksCreatedBy(userId);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/unassigned")
    public ResponseEntity<List<TaskResponseDto>> getUnassignedTasks() {
        List<Task> tasks = taskSearchService.getAllUnassignedTasks();
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/search/high-priority?userId={userId}
     * Get all HIGH priority tasks assigned to a user
     */
    @GetMapping("/search/high-priority")
    public ResponseEntity<List<TaskResponseDto>> getHighPriorityTasks(@RequestParam UUID userId) {
        List<Task> tasks = taskSearchService.getHighPriorityTasksFor(userId);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/search/open?userId={userId}
     * Get all OPEN tasks assigned to a user
     */
    @GetMapping("/search/open")
    public ResponseEntity<List<TaskResponseDto>> getOpenTasks(@RequestParam UUID userId) {
        List<Task> tasks = taskSearchService.getOpenTasksFor(userId);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/search/in-progress?userId={userId}
     * Get all IN_PROGRESS tasks assigned to a user
     */
    @GetMapping("/search/in-progress")
    public ResponseEntity<List<TaskResponseDto>> getInProgressTasks(@RequestParam UUID userId) {
        List<Task> tasks = taskSearchService.getInProgressTasksFor(userId);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/search/urgent-open
     * Get all URGENT (CRITICAL or HIGH) OPEN tasks
     */
    @GetMapping("/search/urgent-open")
    public ResponseEntity<List<TaskResponseDto>> getUrgentOpenTasks() {
        List<Task> tasks = taskSearchService.getUrgentOpenTasks();
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/search/by-date-range?from=2024-01-01&to=2024-12-31
     * Get tasks created between two dates
     */
    @GetMapping("/search/by-date-range")
    public ResponseEntity<List<TaskResponseDto>> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<Task> tasks = taskSearchService.getTasksCreatedBetween(from, to);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/search/by-tags?tags=urgent,backend
     * Get tasks that have ALL specified tags (AND condition)
     */
    @GetMapping("/search/by-tags")
    public ResponseEntity<List<TaskResponseDto>> getTasksByTags(@RequestParam String tags) {
        List<String> tagList = Arrays.stream(tags.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        
        List<Task> tasks = taskSearchService.getTasksWithAllTags(tagList);
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/sorted/by-priority
     * Get all tasks sorted by priority (highest first)
     */
    @GetMapping("/sorted/by-priority")
    public ResponseEntity<List<TaskResponseDto>> getTasksSortedByPriority() {
        List<Task> tasks = taskSearchService.getAllTasksSortedByPriority();
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/sorted/by-due-date
     * Get all tasks sorted by due date (earliest first)
     */
    @GetMapping("/sorted/by-due-date")
    public ResponseEntity<List<TaskResponseDto>> getTasksSortedByDueDate() {
        List<Task> tasks = taskSearchService.getAllTasksSortedByDueDate();
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/sorted/by-created-date
     * Get all tasks sorted by creation date (newest first)
     */
    @GetMapping("/sorted/by-created-date")
    public ResponseEntity<List<TaskResponseDto>> getTasksSortedByCreatedDate() {
        List<Task> tasks = taskSearchService.getAllTasksSortedByCreatedDate();
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/tasks/sorted/by-status
     * Get all tasks sorted by status
     */
    @GetMapping("/sorted/by-status")
    public ResponseEntity<List<TaskResponseDto>> getTasksSortedByStatus() {
        List<Task> tasks = taskSearchService.getAllTasksSortedByStatus();
        List<TaskResponseDto> dtos = tasks.stream()
                .map(TaskConverter::toTaskRespone)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/count/by-status/{status}")
    public ResponseEntity<Long> countTasksByStatus(@PathVariable TaskStatus status) {
        long count = taskSearchService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/tasks/count/overdue
     * Count overdue tasks
     */
    @GetMapping("/count/overdue")
    public ResponseEntity<Long> countOverdueTasks() {
        long count = taskSearchService.countOverdueTasks();
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/tasks/count/assigned-to/{userId}
     * Count tasks assigned to a user
     */
    @GetMapping("/count/assigned-to/{userId}")
    public ResponseEntity<Long> countTasksAssignedTo(@PathVariable UUID userId) {
        long count = taskSearchService.countTasksAssignedTo(userId);
        return ResponseEntity.ok(count);
    }

}
