package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSkillRequest {

    @NotBlank
    @Size(max = 100)
    String name;

    @NotBlank
    @Pattern(
            regexp = "^(BEGINNER|INTERMEDIATE|ADVANCED|EXPERT)$",
            message = "Level must be one of: BEGINNER, INTERMEDIATE, ADVANCED, EXPERT"
    )
    String level;

    @Pattern(
            regexp = "^(SOFT_SKILL|CORE_SKILL|LEADERSHIP_SKILL)$",
            message = "Category must be one of: SOFT_SKILL, CORE_SKILL, LEADERSHIP_SKILL"
    )
    String category;
}
