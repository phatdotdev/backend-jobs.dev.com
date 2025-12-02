package com.dev.job.repository.User;

import com.dev.job.entity.user.Recruiter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RecruiterRepository extends JpaRepository<Recruiter, UUID>, JpaSpecificationExecutor<Recruiter> {
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
    SELECT r
    FROM JobPosting jp
    JOIN jp.recruiter r
    LEFT JOIN jp.applications a
    WHERE jp.state = com.dev.job.entity.posting.PostState.PUBLISHED
      AND jp.expiredAt > CURRENT_TIMESTAMP
    GROUP BY r
    ORDER BY (SUM(jp.views * 0.5 + jp.likes * 2) + COUNT(a) * 5) DESC
    """)
    List<Recruiter> findTopFeaturedRecruiters(Pageable pageable);

}
