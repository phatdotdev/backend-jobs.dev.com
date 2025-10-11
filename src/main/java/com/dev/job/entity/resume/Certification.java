package com.dev.job.entity.resume;

import com.dev.job.entity.user.JobSeeker;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "certification")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String issuer;
    LocalDate issueDate;
    LocalDate expirationDate;
    String credentialId;
    String credentialUrl;
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id")
    JobSeeker jobSeeker;
}
