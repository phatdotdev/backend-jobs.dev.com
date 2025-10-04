package com.dev.job.entity.resume;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;

import java.time.LocalDate;
import java.util.List;


public class Education {
    Long id;

    @Column(name = "school_name", nullable = false, length = 100)
    String schoolName;

    @Column(nullable = false, length = 50)
    String degree;

    @Column(length = 100)
    String major;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(precision = 3, scale = 2)
    double gpa;

    @ManyToMany(mappedBy = "educations")
    List<Resume> resumes;
}
