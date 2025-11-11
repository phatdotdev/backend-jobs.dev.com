package com.dev.job.entity.posting;

import com.dev.job.entity.application.Application;
import com.dev.job.entity.resource.Image;
import com.dev.job.entity.resource.Location;
import com.dev.job.entity.user.Recruiter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_posting")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String title;

    BigDecimal minSalary;
    BigDecimal maxSalary;

    JobType type;
    String experience;

    @Lob
    String description;

    @Lob
    String requirements;

    @Lob
    String benefits;


    @Transient
            String companyName;

    @Transient
            String companyAvatar;

    @JoinColumn(name = "location_id")
    @ManyToOne
    Location location;


    PostState state;

    int views;
    int likes;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    LocalDateTime expiredAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "job_id")
    List<Image> images;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Application> applications = new ArrayList<>();


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    Recruiter recruiter;
}