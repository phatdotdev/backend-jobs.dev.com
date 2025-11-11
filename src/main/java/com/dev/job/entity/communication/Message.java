package com.dev.job.entity.communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "sender_id")
    UUID senderId;
    @Column(name = "receiver_id")
    UUID receiverId;

    String content;

    @JsonProperty("isRead")
    boolean isRead;

    LocalDateTime timestamp;
}
