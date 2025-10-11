package com.dev.job.repository.Posting;

import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.user.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobPostingRepository extends JpaRepository<JobPosting, UUID> {
    List<JobPosting> findByRecruiterId(UUID rId);
}
