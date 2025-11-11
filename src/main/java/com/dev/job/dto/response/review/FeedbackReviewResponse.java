package com.dev.job.dto.response.review;

import com.dev.job.dto.response.Resume.ResumeResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedbackReviewResponse {
    UUID id;
    UUID feedbackRequestId;
    UUID expertId;

    Integer score;

    String overallComment;
    String experienceComment;
    String skillsComment;
    String educationComment;
    String recommendation;

    ResumeResponse resume;

    LocalDateTime createdAt;
}
