package com.dev.job.entity.resume;

import com.dev.job.entity.resource.Link;
import com.dev.job.entity.user.JobSeeker;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "award")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    String organization;
    LocalDate receivedDate;
    String achievement;
    String description;

    @OneToOne
    @JoinColumn(name = "link")
    Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id")
    JobSeeker jobSeeker;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
