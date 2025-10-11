package com.dev.job.dto.response.Resume;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}
