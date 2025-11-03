package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateExperienceRequest {
    UUID id;

    @NotBlank(message = "Company name must not be blank")
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    String companyName;

    @NotBlank(message = "Position must not be blank")
    @Size(max = 255, message = "Position must not exceed 255 characters")
    String position;

    @PastOrPresent(message = "Start date must be in the past or present")
    @NotNull(message = "Start date must not be null")
    LocalDate startDate;

    LocalDate endDate;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
