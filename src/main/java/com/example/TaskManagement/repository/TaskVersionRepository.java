package com.example.TaskManagement.repository;

import com.example.TaskManagement.model.TaskVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskVersionRepository extends JpaRepository<TaskVersion, UUID> {
    List<TaskVersion> findByTaskIdOrderByVersionDesc(UUID taskId);
}
