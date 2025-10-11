package com.dev.job.dto.response.Posting;

import com.dev.job.entity.posting.JobType;
import com.dev.job.entity.posting.PostState;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPostingResponse {
    UUID id;

    String title;
    JobType type;

    String description;
    String requirements;
    String benefits;

    BigDecimal promotedSalary;

    UUID locationId;

    PostState state;

    int views;
    int likes;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime expiredAt;

    String recruiterName;
}
