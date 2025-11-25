package com.dev.job.repository.Expertise;

import com.dev.job.entity.specification.Expertise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpertiseRepository extends JpaRepository<Expertise, UUID> {
    List<Expertise> findByExpertId(UUID id);
}
