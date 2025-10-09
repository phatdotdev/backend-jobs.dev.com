package com.dev.job.repository.Resume;

import com.dev.job.entity.resume.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AwardRepository extends JpaRepository<Award, UUID> {
    List<Award> findByJobSeekerId(UUID jsId);
}
