package com.dev.job.entity.posting;

import com.dev.job.entity.user.Recruiter;
import com.dev.job.enums.JobType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "job_postings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 150)
    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", length = 20)
    JobType jobType;

    String industry;

    List<String> tags;

    @ManyToOne
    Recruiter recruiter;
}
