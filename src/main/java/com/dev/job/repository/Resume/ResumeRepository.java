package com.dev.job.repository.Resume;

import com.dev.job.entity.resume.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    List<Resume> findByJobSeekerId(UUID jsId);
}
