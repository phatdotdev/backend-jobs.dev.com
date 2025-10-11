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
    @NotNull
    @Size(min = 1, max = 255)
    String name;
    @NotNull
    String organization;
    @NotNull
    String role;
    LocalDate startDate;
    LocalDate endDate;
    String description;
}
