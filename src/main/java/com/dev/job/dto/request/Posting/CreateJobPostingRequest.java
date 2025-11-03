package com.dev.job.dto.request.Posting;

import com.dev.job.entity.posting.JobType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateJobPostingRequest {

    @NotBlank(message = "Job title must not be blank.")
    @Size(max = 255, message = "Job title must not exceed 255 characters.")
    String title;

    @NotNull(message = "Job type must not be null.")
    JobType type;

    @NotBlank(message = "Experience requirement must not be blank.")
    String experience;

    @NotNull(message = "Minimum salary must not be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum salary must be greater than 0.")
    BigDecimal minSalary;

    @NotNull(message = "Maximum salary must not be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum salary must be greater than 0.")
    BigDecimal maxSalary;

    @NotBlank(message = "Job description must not be blank.")
    @Size(min = 50, message = "Job description must contain at least 50 characters.")
    String description;

    @NotBlank(message = "Job requirements must not be blank.")
    @Size(min = 50, message = "Job requirements must contain at least 50 characters.")
    String requirements;

    @NotBlank(message = "Job benefits must not be blank.")
    @Size(min = 50, message = "Job benefits must contain at least 50 characters.")
    String benefits;

    @NotNull(message = "Location ID must not be null.")
    UUID locationId;

    @FutureOrPresent(message = "Expiration date must be today or a future date.")
    @NotNull(message = "Expiration date must not be null.")
    LocalDateTime expiredAt;
}