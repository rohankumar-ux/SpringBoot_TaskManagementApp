package com.example.TaskManagement.repository;

import com.example.TaskManagement.enums.Priority;
import com.example.TaskManagement.enums.TaskStatus;
import com.example.TaskManagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    // ==================== BASIC FILTERS ====================

    /**
     * Find tasks by single status
     * SELECT * FROM tasks WHERE status = ?
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find tasks by multiple statuses
     * SELECT * FROM tasks WHERE status IN (?, ?)
     */
    List<Task> findByStatusIn(List<TaskStatus> statuses);

    /**
     * Find tasks by single priority
     * SELECT * FROM tasks WHERE priority = ?
     */
    List<Task> findByPriority(Priority priority);

    /**
     * Find tasks by multiple priorities
     * SELECT * FROM tasks WHERE priority IN (?, ?)
     */
    List<Task> findByPriorityIn(List<Priority> priorities);

    /**
     * Find tasks assigned to a specific user
     * SELECT * FROM tasks WHERE assigned_to = ?
     */
    List<Task> findByAssignedToId(UUID userId);

    /**
     * Find unassigned tasks
     * SELECT * FROM tasks WHERE assigned_to IS NULL
     */
    List<Task> findByAssignedToIsNull();

    /**
     * Find tasks created by a specific user
     * SELECT * FROM tasks WHERE created_by = ?
     */
    List<Task> findByCreatedById(UUID userId);

    // ==================== OVERDUE TASKS ====================

    /**
     * Find overdue tasks (due date is past and not completed/cancelled)
     * Custom JPQL query
     */
    @Query("SELECT t FROM Task t " +
           "WHERE t.dueDate < CURRENT_DATE " +
           "AND t.status != com.example.TaskManagement.enums.TaskStatus.COMPLETED " +
           "AND t.status != com.example.TaskManagement.enums.TaskStatus.CANCELLED " +
           "ORDER BY t.dueDate ASC")
    List<Task> findOverdueTasks();

    // ==================== DATE RANGE FILTERS ====================

    /**
     * Find tasks created between two dates
     * CAST converts LocalDateTime to DATE for comparison
     */
    @Query("SELECT t FROM Task t " +
           "WHERE CAST(t.createdAt AS date) BETWEEN :startDate AND :endDate " +
           "ORDER BY t.createdAt DESC")
    List<Task> findTasksCreatedBetween(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    /**
     * Find tasks updated between two dates
     */
    @Query("SELECT t FROM Task t " +
           "WHERE CAST(t.updatedAt AS date) BETWEEN :startDate AND :endDate " +
           "ORDER BY t.updatedAt DESC")
    List<Task> findTasksUpdatedBetween(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    /**
     * Find completed tasks between two dates
     */
    @Query("SELECT t FROM Task t " +
           "WHERE t.status = com.example.TaskManagement.enums.TaskStatus.COMPLETED " +
           "AND CAST(t.updatedAt AS date) BETWEEN :startDate AND :endDate " +
           "ORDER BY t.updatedAt DESC")
    List<Task> findCompletedTasksBetween(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    // ==================== TAG FILTERS ====================

    /**
     * Find tasks with ALL specified tags (AND condition)
     * Uses native query because JPA doesn't handle element collections well
     * GROUP BY ensures the task has ALL tags (count matches tag count)
     */
    @Query(value = "SELECT DISTINCT t.* FROM tasks t " +
                   "LEFT JOIN task_tags tt ON t.id = tt.task_id " +
                   "WHERE tt.tag IN :tags " +
                   "GROUP BY t.id " +
                   "HAVING COUNT(DISTINCT tt.tag) = :tagCount " +
                   "ORDER BY t.created_at DESC",
           nativeQuery = true)
    List<Task> findTasksWithAllTags(@Param("tags") List<String> tags,
                                     @Param("tagCount") Long tagCount);

    /**
     * Find tasks with ANY of the specified tags (OR condition)
     * Uses native query because JPA doesn't handle element collections well
     */
    @Query(value = "SELECT DISTINCT t.* FROM tasks t " +
                   "LEFT JOIN task_tags tt ON t.id = tt.task_id " +
                   "WHERE tt.tag IN :tags " +
                   "ORDER BY t.created_at DESC",
           nativeQuery = true)
    List<Task> findTasksWithAnyTag(@Param("tags") List<String> tags);

    // ==================== COMBINED FILTERS ====================

    /**
     * Find tasks with specific status and priority
     */
    List<Task> findByStatusAndPriority(TaskStatus status, Priority priority);

    /**
     * Find tasks with specific status and assigned to user
     */
    List<Task> findByStatusAndAssignedToId(TaskStatus status, UUID userId);

    /**
     * Find tasks with specific priority and assigned to user
     */
    List<Task> findByPriorityAndAssignedToId(Priority priority, UUID userId);

    /**
     * Find urgent open tasks (OPEN status with HIGH or CRITICAL priority)
     * Sorted by due date (earliest first)
     */
    @Query("SELECT t FROM Task t " +
           "WHERE t.status = com.example.TaskManagement.enums.TaskStatus.OPEN " +
           "AND (t.priority = com.example.TaskManagement.enums.Priority.HIGH " +
           "OR t.priority = com.example.TaskManagement.enums.Priority.CRITICAL) " +
           "ORDER BY t.dueDate ASC")
    List<Task> findUrgentOpenTasks();

    // ==================== SORTING METHODS ====================

    /**
     * Get all tasks sorted by priority (highest/critical first)
     */
    @Query("SELECT t FROM Task t ORDER BY t.priority DESC, t.createdAt DESC")
    List<Task> findAllOrderByPriorityDesc();

    /**
     * Get all tasks sorted by due date (earliest first)
     */
    @Query("SELECT t FROM Task t ORDER BY t.dueDate ASC, t.priority DESC")
    List<Task> findAllOrderByDueDateAsc();

    /**
     * Get all tasks sorted by created date (newest first)
     */
    @Query("SELECT t FROM Task t ORDER BY t.createdAt DESC")
    List<Task> findAllOrderByCreatedAtDesc();

    /**
     * Get all tasks sorted by status
     */
    @Query("SELECT t FROM Task t ORDER BY t.status ASC, t.priority DESC")
    List<Task> findAllOrderByStatus();

    /**
     * Get all tasks sorted by status then by priority
     */
    @Query("SELECT t FROM Task t ORDER BY t.status ASC, t.priority DESC, t.dueDate ASC")
    List<Task> findAllOrderByStatusAndPriority();

    // ==================== COUNT METHODS ====================

    /**
     * Count tasks by status
     */
    long countByStatus(TaskStatus status);

    /**
     * Count overdue tasks
     */
    @Query("SELECT COUNT(t) FROM Task t " +
           "WHERE t.dueDate < CURRENT_DATE " +
           "AND t.status != com.example.TaskManagement.enums.TaskStatus.COMPLETED " +
           "AND t.status != com.example.TaskManagement.enums.TaskStatus.CANCELLED")
    long countOverdueTasks();

    /**
     * Count tasks assigned to a user
     */
    long countByAssignedToId(UUID userId);

    /**
     * Count tasks created by a user
     */
    long countByCreatedById(UUID userId);
}
