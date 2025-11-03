package com.dev.job.dto.response.Resume;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillResponse {
    String id;
    String name;
    String level;
    String category;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
