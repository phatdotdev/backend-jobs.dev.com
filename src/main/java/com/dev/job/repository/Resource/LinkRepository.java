package com.dev.job.repository.Resource;

import com.dev.job.entity.resource.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
}
