package com.dev.job.repository.Resource;

import com.dev.job.entity.resource.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
}
