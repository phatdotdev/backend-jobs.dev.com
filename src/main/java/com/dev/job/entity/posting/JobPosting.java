package com.dev.job.entity.posting;

import com.dev.job.entity.resource.Location;
import com.dev.job.entity.user.Recruiter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "job_posting")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String title;
    JobType type;

    @Lob
    String description;

    @Lob
    String requirements;

    @Lob
    String benefits;

    BigDecimal promotedSalary;

    @JoinColumn(name = "location_id")
    @ManyToOne
    Location location;


    PostState state;

    int views;
    int likes;

    LocalDate createdAt;
    LocalDate updatedAt;
    LocalDate expiredAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    Recruiter recruiter;
}
