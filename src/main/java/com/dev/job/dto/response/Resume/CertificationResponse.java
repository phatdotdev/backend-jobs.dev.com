package com.dev.job.dto.response.Resume;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificationResponse {
    String id;
    String name;
    String issuer;
    LocalDate issueDate;
    LocalDate expirationDate;
    String credentialId;
    String credentialUrl;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
