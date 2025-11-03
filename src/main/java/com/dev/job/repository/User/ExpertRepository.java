package com.dev.job.repository.User;

import com.dev.job.entity.user.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ExpertRepository extends JpaRepository<Expert, UUID>, JpaSpecificationExecutor<Expert> {
}
