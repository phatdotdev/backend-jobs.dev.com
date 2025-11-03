package com.dev.job.repository.Resource;

import com.dev.job.entity.resource.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
}
