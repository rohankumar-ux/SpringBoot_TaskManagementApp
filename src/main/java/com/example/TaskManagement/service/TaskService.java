package com.example.TaskManagement.service;

import com.example.TaskManagement.converter.CommentConverter;
import com.example.TaskManagement.converter.TaskConverter;
import com.example.TaskManagement.converter.TaskVersionConverter;
import com.example.TaskManagement.dto.*;
import com.example.TaskManagement.enums.ActivityType;
import com.example.TaskManagement.enums.Priority;
import com.example.TaskManagement.enums.TaskStatus;
import com.example.TaskManagement.exception.InvalidStateTransitionException;
import com.example.TaskManagement.exception.ResourceNotFoundException;
import com.example.TaskManagement.model.*;
import com.example.TaskManagement.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskVersionRepository taskVersionRepository;
    private final ActivityEventRepository activityEventRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public TaskResponseDto createTask(CreateTaskRequestDto request){
        User creator = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getCreatedBy()));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreatedBy(creator);
        task.setStatus(TaskStatus.OPEN);
        task.setPriority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM);
        task.setVersion(1);

        if(request.getAssignedTo() != null){
            User assignee = userRepository.findById(request.getAssignedTo())
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getCreatedBy()));
            task.setAssignedTo(assignee);
        }

        if(request.getDueDate() != null){
            task.setDueDate(request.getDueDate());
        }

        if(request.getTags() != null){
            task.setTags(request.getTags());
        }

        Task createdTask = taskRepository.save(task);

        createTaskVersion(createdTask , "Task Created");

        createActivityEvent(createdTask.getId() , ActivityType.TASK_CREATED , creator , "Task Created");

        return TaskConverter.toTaskRespone(createdTask);
    }

    @Transactional
    public TaskResponseDto getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id :" + id));
        return TaskConverter.toTaskRespone(task);
    }

    @Transactional
    public TaskResponseDto updateTaskStatus(UUID id,UpdateTaskStatusRequestDto request, UUID performedBy) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id :" + id));

        User performer = userRepository.findById(performedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id:" + performedBy));

        if(!isValidTransition(task.getStatus() , request.getStatus())){
            throw new InvalidStateTransitionException("Invalid status transition from " + task.getStatus() + " to " + request.getStatus());
        }

        TaskStatus oldStatus = task.getStatus();
        task.setStatus(request.getStatus());
        task.setVersion(task.getVersion() + 1);

        Task updatedTask = taskRepository.save(task);
        String details = "Status changed from " + oldStatus + " to " + request.getStatus();
        createTaskVersion(updatedTask , details);

        createActivityEvent(updatedTask.getId() , ActivityType.STATUS_CHANGED , performer , details);

        return TaskConverter.toTaskRespone(updatedTask);
    }

    @Transactional
    public TaskResponseDto assignTask(UUID id,UpdateTaskAssigneeRequestDto request, UUID performedBy) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id :" + id));

        User performer = userRepository.findById(performedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id:" + performedBy));

        String details;
        if(request.getAssignedTo() != null){
            User assignee = userRepository.findById(request.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedTo()));
            task.setAssignedTo(assignee);
            details = "Task assigned to " + assignee.getName();
        }else{
            task.setAssignedTo(null);
            details = "Task unassigned";
        }

        task.setVersion(task.getVersion() + 1);
        Task updatedTask = taskRepository.save(task);

        createTaskVersion(updatedTask , details);
        createActivityEvent(updatedTask.getId() , ActivityType.ASSIGNEE_CHANGED , performer , details);

        return TaskConverter.toTaskRespone(updatedTask);
    }

    @Transactional
    public TaskResponseDto updateTaskPriority(UUID id,UpdateTaskPriorityRequestDto request, UUID performedBy) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id :" + id));

        User performer = userRepository.findById(performedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id:" + performedBy));

        Priority oldPriority = task.getPriority();
        task.setPriority(request.getPriority());
        task.setVersion(task.getVersion() + 1);

        Task updatedTask = taskRepository.save(task);

        String details = "Priority changed from " + oldPriority + " to " + request.getPriority();
        createTaskVersion(updatedTask , details);
        createActivityEvent(updatedTask.getId() , ActivityType.PRIORITY_CHANGED , performer , details);

        return TaskConverter.toTaskRespone(updatedTask);
    }

    @Transactional
    public TaskResponseDto updateTaskDueDate(UUID id,UpdateTaskDueDateRequestDto request, UUID performedBy) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id :" + id));

        User performer = userRepository.findById(performedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id:" + performedBy));

        LocalDate oldDate = task.getDueDate();
        task.setDueDate(request.getDueDate());
        task.setVersion(task.getVersion() + 1);

        Task updatedTask = taskRepository.save(task);

        String details = "Due Date changed from " + oldDate + " to " + request.getDueDate();
        createTaskVersion(updatedTask , details);
        createActivityEvent(updatedTask.getId() , ActivityType.DUE_DATE_CHANGED , performer , details);

        return TaskConverter.toTaskRespone(updatedTask);
    }

    @Transactional
    public CommentResponseDto addComment(UUID id, @Valid AddCommentRequestDto request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id :" + id));

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id:" + request.getAuthorId()));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setText(request.getText());

        commentRepository.save(comment);

        createActivityEvent(id , ActivityType.COMMENT_ADDED , author , "Comment added by : " + author.getName());

        return CommentConverter.toCommentResponse(comment);
    }

    @Transactional
    public List<TaskVersionResponseDto> getTaskHistory(UUID id) {
        if(!taskRepository.existsById(id)){
            throw new ResourceNotFoundException("Task not found with id :" + id);
        }

        List<TaskVersion> versions = taskVersionRepository.findByTaskIdOrderByVersionDesc(id);

        return versions.stream()
                .map(TaskVersionConverter:: toTaskVersionResponseDto)
                .collect(Collectors.toList());
    }

    private boolean isValidTransition(TaskStatus from,TaskStatus to) {
        if(from == to){
            return true;
        }

        return switch(from){
            case OPEN -> to == TaskStatus.IN_PROGRESS || to == TaskStatus.CANCELLED;
            case IN_PROGRESS -> to == TaskStatus.COMPLETED || to == TaskStatus.CANCELLED;
            case CANCELLED -> to == TaskStatus.OPEN;
            case COMPLETED -> false;
        };
    }

    public TaskResponseDto convertToDto(Task task) {
        return TaskConverter.toTaskRespone(task);
    }

    private void createTaskVersion(Task task, String changeSummary) {
        TaskVersion version = new TaskVersion();
        version.setTaskId(task.getId());
        version.setVersion(task.getVersion());
        version.setTitle(task.getTitle());
        version.setDescription(task.getDescription());
        version.setStatus(task.getStatus());
        version.setPriority(task.getPriority());
        version.setCreatedBy(task.getCreatedBy().getId());
        version.setAssignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null);
        version.setDueDate(task.getDueDate());
        version.setTags(new ArrayList<>(task.getTags()));
        version.setChangeSummary(changeSummary);

        taskVersionRepository.save(version);
    }

    private void createActivityEvent(UUID taskId, ActivityType type,
                                     User performer, String details) {
        ActivityEvent event = new ActivityEvent();
        event.setTaskId(taskId);
        event.setActivityType(type);
        event.setPerformedBy(performer);
        event.setDetails(details);

        activityEventRepository.save(event);
    }

}
