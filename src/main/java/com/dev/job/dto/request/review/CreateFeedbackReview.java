package com.dev.job.dto.request.review;

public record CreateFeedbackReview(
        Integer score,
        String overallComment,
        String experienceComment,
        String skillsComment,
        String educationComment,
        String recommendation
) {
}
