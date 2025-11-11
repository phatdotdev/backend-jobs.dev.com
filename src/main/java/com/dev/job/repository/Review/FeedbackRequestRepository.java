package com.dev.job.repository.Review;

import com.dev.job.entity.review.FeedbackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, UUID> {
    List<FeedbackRequest> findByResume_Id(UUID resumeId);
    Page<FeedbackRequest> findByCompletedAtAfter(LocalDateTime now, Pageable pageable);
}
