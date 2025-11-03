package com.dev.job.repository.Resource;

import com.dev.job.entity.resource.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
