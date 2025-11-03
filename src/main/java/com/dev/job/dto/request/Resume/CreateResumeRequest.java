package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateResumeRequest {
    @NotBlank(message = "Title must not be null.")
    String title;
    @NotBlank(message = "Introduction must not be null.")
    String introduction;
    @NotBlank(message = "Object career must not be null.")
    String objectCareer;

    List<UUID> educations;
    List<UUID> experiences;
    List<UUID> certifications;
    List<UUID> skills;
    List<UUID> projects;
    List<UUID> awards;
    List<UUID> activities;
}
