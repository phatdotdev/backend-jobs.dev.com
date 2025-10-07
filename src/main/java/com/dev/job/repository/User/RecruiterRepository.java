package com.dev.job.repository.User;

import com.dev.job.entity.user.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecruiterRepository extends JpaRepository<Recruiter, UUID> {
}
