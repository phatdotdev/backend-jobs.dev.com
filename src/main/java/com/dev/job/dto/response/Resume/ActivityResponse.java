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
