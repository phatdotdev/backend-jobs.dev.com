package com.dev.job.repository.Resume;

import com.dev.job.entity.resume.Education;
import com.dev.job.entity.resume.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    List<Skill> findByJobSeekerId(UUID jsId);
}
