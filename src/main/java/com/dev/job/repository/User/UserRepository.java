package com.dev.job.repository.User;

import com.dev.job.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
