package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEducationRequest {

    @NotNull(message = "ID must not be null")
    UUID id;

    @NotBlank(message = "School name must not be blank")
    @Size(min = 6, max = 255, message = "School name must be between 6 and 255 characters")
    String schoolName;

    @NotBlank(message = "Degree must not be blank")
    @Size(max = 255, message = "Degree must not exceed 255 characters")
    String degree;

    @Size(max = 255, message = "Major must not exceed 255 characters")
    String major;

    @PastOrPresent(message = "Start date must be in the past or present")
    @NotNull(message = "Start date must not be null")
    LocalDate startDate;

    LocalDate endDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Grade must be at least 0.0")
    @DecimalMax(value = "4.0", inclusive = true, message = "Grade must not exceed 4.0")
    Double grade;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
