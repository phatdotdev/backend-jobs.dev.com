package com.dev.job.dto.response.Communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    UUID id;

    String senderId;
    String senderUsername;
    String receiverId;
    String receiverUsername;
    @JsonProperty("isRead")
    boolean isRead;

    String content;
    LocalDateTime timestamp;
}
