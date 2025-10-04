package com.dev.job.entity.resume;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(name = "activity_name")
    String name;

    String role;

    @JoinColumn(name = "start_date")
    LocalDate startDate;

    @JoinColumn(name = "end_date")
    LocalDate endDate;

    String description;
}
