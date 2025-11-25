package com.dev.job.repository.Posting;

import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.posting.PostState;
import com.dev.job.entity.user.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobPostingRepository extends JpaRepository<JobPosting, UUID>, JpaSpecificationExecutor<JobPosting> {
    List<JobPosting> findByRecruiterId(UUID rId);
    Page<JobPosting> findByRecruiterId(UUID rId, Pageable pageable);
    Page<JobPosting> findByRecruiterIdAndState(UUID rId, PostState state, Pageable pageable);
    List<JobPosting> findTop10ByRecruiterIdOrderByCreatedAtDesc(UUID recruiterId);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<JobPosting> findByState(PostState state);

    Long countByRecruiterId(UUID recruiterId);
    Long countByRecruiterIdAndState(UUID recruiterId, PostState state);
}
