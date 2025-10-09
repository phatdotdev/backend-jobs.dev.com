package com.dev.job.repository.Resume;

import com.dev.job.entity.resume.Activity;
import com.dev.job.entity.resume.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByJobSeekerId(UUID jsId);
}
