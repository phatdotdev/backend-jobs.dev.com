package com.dev.job.repository.User;

import com.dev.job.entity.user.JobSeeker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, UUID>, JpaSpecificationExecutor<JobSeeker> {
    List<JobSeeker> findByRecommendedTrue();
    List<JobSeeker> findByRecommendedTrue(Pageable pageable);
}
