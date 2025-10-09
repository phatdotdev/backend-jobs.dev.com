package com.dev.job.entity.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "experience")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkExperience {
    @Id
    UUID id;
    @Column(name = "company_name")
    String companyName;
    String position;
    @Column(name = "start_date")
    LocalDate startDate;
    @Column(name = "end_date")
    LocalDate endDate;
    String description;
}
