package com.dev.job.dto.response.Communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    UUID id;
    UUID senderId;
    UUID recipientId;
    String type;
    String title;
    String content;
    @JsonProperty("isRead")
    boolean isRead;
    LocalDateTime timestamp;
    String objectTitle;

    UUID applicationId;
    UUID postId;
    UUID resumeId;
}
