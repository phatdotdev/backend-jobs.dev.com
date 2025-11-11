package com.dev.job.entity.communication;

import com.dev.job.entity.application.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="notification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    UUID senderId;

    UUID recipientId;
    NotificationType type;
    String title;
    String content;

    @JsonProperty("isRead")
    boolean isRead;
    LocalDateTime timestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = true)
    private Application application;
}