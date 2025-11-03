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

    @NotBlank(message = "Skill name must not be blank")
    @Size(max = 100, message = "Skill name must not exceed 100 characters")
    String name;

    @NotBlank(message = "Skill level must not be blank")
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

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
