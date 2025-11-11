package com.dev.job.dto.response.review;

import com.dev.job.dto.response.Resume.ResumeResponse;
import com.dev.job.entity.review.FeedbackStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedbackRequestResponse {
    UUID id;
    ResumeResponse resume;

    String notes;
    FeedbackStatus status;

    LocalDateTime createdAt;
    LocalDateTime completedAt;

    List<FeedbackReviewResponse> reviews;

}
