package com.dev.job.repository.Application;

import com.dev.job.entity.application.Application;
import com.dev.job.entity.application.ApplicationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Optional<Application> findByJobPostingIdAndResume_JobSeeker_Id(UUID postId, UUID jobSeekerId);
    Page<Application> findByResume_JobSeeker_Id(UUID jsId, Pageable pageable);
    Page<Application> findByJobPostingId(UUID postId, Pageable pageable);
    Page<Application> findByJobPostingIdAndState(UUID postId, ApplicationState state, Pageable pageable);
    boolean existsByJobPostingIdAndResumeId(UUID postId, UUID resumeId);
}
