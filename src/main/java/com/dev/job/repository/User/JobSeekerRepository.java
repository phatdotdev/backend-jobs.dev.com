package com.dev.job.repository.User;

import com.dev.job.entity.user.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, UUID>, JpaSpecificationExecutor<JobSeeker> {
}
