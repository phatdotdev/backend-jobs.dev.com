package com.dev.job.dto.response.Resume;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityResponse {
    String id;
    String name;
    String organization;
    String role;
    LocalDate startDate;
    LocalDate endDate;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
