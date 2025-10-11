package com.dev.job.dto.request.Posting;

import com.dev.job.entity.posting.JobType;
import com.dev.job.entity.posting.PostState;
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

    @NotBlank
    @Size(max = 255)
    String title;

    @NotNull
    JobType type;

    @NotBlank
    String description;

    @NotBlank
    String requirements;

    String benefits;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal promotedSalary;

    @NotNull
    UUID locationId;

    @NotNull
    PostState state;

    @FutureOrPresent
    LocalDateTime expiredAt;
}
