package com.example.TaskManagement.service;

import com.example.TaskManagement.enums.Priority;
import com.example.TaskManagement.enums.TaskStatus;
import com.example.TaskManagement.model.Task;
import com.example.TaskManagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TaskSearchService {

    private final TaskRepository taskRepository;


    public List<Task> getAllTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getAllTasksByStatuses(List<TaskStatus> statuses) {
        return taskRepository.findByStatusIn(statuses);
    }

    
    public List<Task> getAllTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> getAllTasksByPriorities(List<Priority> priorities) {
        return taskRepository.findByPriorityIn(priorities);
    }

    public List<Task> getAllTasksAssignedTo(UUID userId) {
        return taskRepository.findByAssignedToId(userId);
    }

    public List<Task> getAllUnassignedTasks() {
        return taskRepository.findByAssignedToIsNull();
    }

    public List<Task> getAllTasksCreatedBy(UUID userId) {
        return taskRepository.findByCreatedById(userId);
    }

    public List<Task> getAllOverdueTasks() {
        return taskRepository.findOverdueTasks();
    }

    public List<Task> getTasksCreatedBetween(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findTasksCreatedBetween(startDate, endDate);
    }

    public List<Task> getTasksUpdatedBetween(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findTasksUpdatedBetween(startDate, endDate);
    }

    public List<Task> getCompletedTasksBetween(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findCompletedTasksBetween(startDate, endDate);
    }

    public List<Task> getTasksWithAllTags(List<String> tags) {
        return taskRepository.findTasksWithAllTags(tags, (long) tags.size());
    }

    public List<Task> getTasksWithAnyTag(List<String> tags) {
        return taskRepository.findTasksWithAnyTag(tags);
    }

    public List<Task> getHighPriorityTasksFor(UUID userId) {
        return taskRepository.findByPriorityAndAssignedToId(Priority.HIGH, userId);
    }

    public List<Task> getOpenTasksFor(UUID userId) {
        return taskRepository.findByStatusAndAssignedToId(TaskStatus.OPEN, userId);
    }

    public List<Task> getInProgressTasksFor(UUID userId) {
        return taskRepository.findByStatusAndAssignedToId(TaskStatus.IN_PROGRESS, userId);
    }

    public List<Task> getTasksByStatusAndPriority(TaskStatus status, Priority priority) {
        return taskRepository.findByStatusAndPriority(status, priority);
    }

    public List<Task> getUrgentOpenTasks() {
        return taskRepository.findUrgentOpenTasks();
    }


    public List<Task> getAllTasksSortedByPriority() {
        return taskRepository.findAllOrderByPriorityDesc();
    }

    public List<Task> getAllTasksSortedByDueDate() {
        return taskRepository.findAllOrderByDueDateAsc();
    }


    public List<Task> getAllTasksSortedByCreatedDate() {
        return taskRepository.findAllOrderByCreatedAtDesc();
    }

    public List<Task> getAllTasksSortedByStatus() {
        return taskRepository.findAllOrderByStatus();
    }

    public long countByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

    public long countOverdueTasks() {
        return taskRepository.countOverdueTasks();
    }

    public long countTasksAssignedTo(UUID userId) {
        return taskRepository.countByAssignedToId(userId);
    }

    public List<Task> getAllTasksSortedByStatusAndPriority() {
        return taskRepository.findAllOrderByStatusAndPriority();
    }
}
