package com.dev.job.repository.Resume;

import com.dev.job.entity.resume.Education;
import com.dev.job.entity.resume.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExperienceRepository extends JpaRepository<WorkExperience, UUID> {
    List<WorkExperience> findByJobSeekerId(UUID jsId);
}
