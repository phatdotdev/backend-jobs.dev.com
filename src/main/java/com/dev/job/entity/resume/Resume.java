package com.dev.job.entity.resume;

import com.dev.job.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 100)
    String title;

    @Column(columnDefinition = "TEXT")
    String summary;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User owner;

    @ManyToMany
    @JoinTable(
        name = "resume_educations",
        joinColumns = @JoinColumn(name = "resume_id"),
        inverseJoinColumns = @JoinColumn(name = "education_id")
    )
    List<Education> educations;

    @ManyToMany
    @JoinTable(
        name = "resume_experiences",
        joinColumns = @JoinColumn(name = "resume_id"),
        inverseJoinColumns = @JoinColumn(name = "experience_id")
    )
    List<WorkExperience> experiences;

    @ManyToMany
    @JoinTable(
        name = "resume_skills",
        joinColumns = @JoinColumn(name = "resume_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    List<Skill> skills;

    @ManyToMany
    @JoinTable(
        name = "resume_certifications",
        joinColumns = @JoinColumn(name = "resume_id"),
        inverseJoinColumns = @JoinColumn(name = "certification_id")
    )
    List<Certification> certifications;

    @ManyToMany
    @JoinTable(
        name = "resume_awards",
        joinColumns = @JoinColumn(name = "resume_id"),
        inverseJoinColumns = @JoinColumn(name = "award_id")
    )
    List<Award> awards;

    @ManyToMany
    @JoinTable(
        name = "resume_activities",
        joinColumns = @JoinColumn(name = "resume_id"),
        inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    List<Activity> activities;

    @Column(name = "create_at")
    LocalDate createAt;

    @Column(name = "update_at")
    LocalDate updateAt;
}
