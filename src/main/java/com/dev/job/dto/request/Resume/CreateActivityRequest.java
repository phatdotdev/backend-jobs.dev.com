package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateActivityRequest {

    @NotNull(message = "Activity name must not be null")
    @Size(min = 1, max = 255, message = "Activity name must be between 1 and 255 characters")
    String name;

    @NotNull(message = "Organization must not be null")
    String organization;

    @NotNull(message = "Role must not be null")
    String role;

    LocalDate startDate;

    LocalDate endDate;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
