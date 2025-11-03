package com.dev.job.dto.request.Posting;

import com.dev.job.entity.posting.JobType;
import com.dev.job.entity.posting.PostState;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobPostingRequest {

    @Size(max = 255, message = "Job title must not exceed 255 characters.")
    String title;

    JobType type;

    String experience;

    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum salary must be greater than 0.")
    BigDecimal minSalary;

    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum salary must be greater than 0.")
    BigDecimal maxSalary;

    String description;
    String requirements;
    String benefits;

    UUID locationId;
    PostState state;

    @FutureOrPresent(message = "Expiration date must be today or a future date.")
    LocalDateTime expiredAt;
}