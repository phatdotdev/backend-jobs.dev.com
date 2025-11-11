package com.dev.job.entity.user;

import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.resume.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "job_seeker")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class JobSeeker extends User{
    String firstname;
    String lastname;
    String phone;
    String address;
    String gender;
    LocalDate dob;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Education> educations;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<WorkExperience> experiences;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Certification> certifications;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Skill> skills;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Award> awards;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Project> projects;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "job_seeker_likes",
            joinColumns = @JoinColumn(name = "job_seeker_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    Set<JobPosting> likes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "job_seeker_views",
            joinColumns = @JoinColumn(name = "job_seeker_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    Set<JobPosting> views = new HashSet<>();
}