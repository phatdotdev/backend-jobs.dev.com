package com.dev.job.dto.request.review;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateFeedbackRequest(
        UUID resumeId,
        String notes,
        LocalDateTime completedAt
) {
}
