package com.dev.job.entity.resume;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "experiences")
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @Column(name = "company_name")
    String name;


    String position;

    LocalDate startDate;
    LocalDate endDate;

    String description;
}
