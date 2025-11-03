package com.dev.job.repository.Resource;

import com.dev.job.entity.resource.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchoolRepository extends JpaRepository<School, UUID> {
}
