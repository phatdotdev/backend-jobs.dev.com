package com.dev.job.entity.resume;

import com.dev.job.entity.user.JobSeeker;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "education")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(name = "school_name")
    String schoolName;
    String degree;
    String major;
    @Column(name = "start_date")
    LocalDate startDate;
    @Column(name = "end_date")
    LocalDate endDate;
    Double grade;
    String description;
    boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id")
    JobSeeker jobSeeker;
}
