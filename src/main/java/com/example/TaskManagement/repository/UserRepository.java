package com.example.TaskManagement.repository;


import com.example.TaskManagement.enums.Role;
import com.example.TaskManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByActive(Boolean active);

    @Query(
            value = """
        SELECT COUNT(*)
        FROM tasks t
        WHERE t.assigned_to = :userId
        AND t.status NOT IN ('COMPLETED', 'CANCELLED')
        """,
            nativeQuery = true
    )
    long countActiveTasks(UUID userId);


}
