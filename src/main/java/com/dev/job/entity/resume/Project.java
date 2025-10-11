package com.dev.job.entity.resume;

import com.dev.job.entity.resource.Link;
import com.dev.job.entity.user.JobSeeker;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "project")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;
    String description;

    String role;
    String result;

    @OneToOne
    @JoinColumn(name = "link_id")
    Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id")
    JobSeeker jobSeeker;
}
