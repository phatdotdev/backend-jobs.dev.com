package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProjectRequest {

    @NotBlank
    @Size(max = 255)
    String name;

    @Size(max = 1000)
    String description;

    @NotBlank
    @Size(max = 255)
    String role;

    @Size(max = 500)
    String result;
}
