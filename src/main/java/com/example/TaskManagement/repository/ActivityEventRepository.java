package com.example.TaskManagement.repository;

import com.example.TaskManagement.model.ActivityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityEventRepository extends JpaRepository<ActivityEvent, UUID> {
    List<ActivityEvent> findByTaskIdOrderByTimestampDesc(UUID taskId);
}
