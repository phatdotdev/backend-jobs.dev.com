package com.dev.job.repository.Application;

import com.dev.job.entity.application.Application;
import com.dev.job.entity.application.ApplicationState;
import com.dev.job.entity.posting.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Optional<Application> findByJobPostingIdAndResume_JobSeeker_Id(UUID postId, UUID jobSeekerId);
    Page<Application> findByResume_JobSeeker_Id(UUID jsId, Pageable pageable);
    Page<Application> findByJobPostingId(UUID postId, Pageable pageable);
    Page<Application> findByJobPostingIdAndState(UUID postId, ApplicationState state, Pageable pageable);
    boolean existsByJobPostingIdAndResumeId(UUID postId, UUID resumeId);
    Long countByAppliedAtBetween(LocalDateTime start, LocalDateTime end);
    Long countByJobPostingRecruiterId(UUID recruiterId);
    Long countByJobPostingRecruiterIdAndAppliedAtBetween(UUID recruiterId, LocalDateTime start, LocalDateTime end);
    Long countByJobPostingRecruiterIdAndState(UUID recruiterId, ApplicationState state);

    List<Application> findByResume_JobSeeker_Id(UUID jsId);

    @Query("SELECT a.jobPosting FROM Application a WHERE a.resume.jobSeeker.id = :userId")
    List<JobPosting> findJobPostingsByUserId(@Param("userId") UUID userId);
}
