package com.dev.job.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String username;

    String email;

    String password;

    UserRole role;

    UserStatus status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    User updatedBy;
}