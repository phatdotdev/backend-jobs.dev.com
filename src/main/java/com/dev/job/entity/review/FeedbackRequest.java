package com.dev.job.entity.review;

import com.dev.job.entity.resume.Resume;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "feedback_request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    Resume resume;

    @Enumerated(EnumType.STRING)
    FeedbackStatus status;

    @Column(columnDefinition = "TEXT")
    String notes;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "completed_at")
    LocalDateTime completedAt;

    @Builder.Default
    @OneToMany(mappedBy = "feedbackRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    List<FeedbackReview> reviews = new ArrayList<>();
}
