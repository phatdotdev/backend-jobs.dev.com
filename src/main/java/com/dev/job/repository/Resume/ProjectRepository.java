package com.dev.job.repository.Resume;

import com.dev.job.entity.resume.Education;
import com.dev.job.entity.resume.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByJobSeekerId(UUID jsId);
}
