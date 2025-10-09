package com.dev.job.entity.user;

import com.dev.job.entity.resource.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    String username;

    @NotNull
    String email;

    @NotNull
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
    @JsonIgnore
    User updatedBy;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    Image avatar;

    @OneToOne
    @JoinColumn(name = "cover_id")
    Image cover;
}