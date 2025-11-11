package com.dev.job.entity.review;

import com.dev.job.entity.user.Expert;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feedback_review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "feedback_request_id", nullable = false)
    FeedbackRequest feedbackRequest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "expert_id", nullable = false)
    Expert expert;

    @Column(nullable = false)
    @Min(0)
    @Max(100)
    Integer score;

    @Column(columnDefinition = "TEXT")
    String overallComment;

    @Column(columnDefinition = "TEXT")
    String experienceComment;

    @Column(columnDefinition = "TEXT")
    String skillsComment;

    @Column(columnDefinition = "TEXT")
    String educationComment;

    @Column(columnDefinition = "TEXT")
    String recommendation;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}
