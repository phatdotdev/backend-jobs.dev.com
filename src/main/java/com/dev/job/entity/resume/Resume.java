package com.dev.job.entity.resume;

import com.dev.job.entity.user.JobSeeker;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "resume")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String introduction;

    @Column(name = "object_career")
    String objectCareer;

    @ManyToMany
    List<Education> educations;

    @ManyToMany
    List<WorkExperience> experiences;

    @ManyToMany
    List<Certification> certifications;

    @ManyToMany
    List<Skill> skills;

    @ManyToMany
    List<Award> awards;

    @ManyToMany
    List<Activity> activities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id")
    JobSeeker jobSeeker;
}
