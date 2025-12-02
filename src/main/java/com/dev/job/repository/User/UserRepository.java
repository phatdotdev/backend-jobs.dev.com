package com.dev.job.repository.User;

import com.dev.job.entity.user.User;
import com.dev.job.entity.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername(String email, String username);

    @Query("SELECT u.id FROM User u")
    List<UUID> findAllUserIds();

    // Statistics
    Long countByStatus(UserStatus status);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
