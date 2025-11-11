package com.dev.job.dto.response.Posting;

import com.dev.job.entity.application.Application;
import com.dev.job.entity.posting.JobType;
import com.dev.job.entity.posting.PostState;
import com.dev.job.entity.resource.Location;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPostingResponse {
    UUID id;

    String title;
    JobType type;

    String companyName;

    String experience;

    BigDecimal minSalary;
    BigDecimal maxSalary;

    String description;
    String requirements;
    String benefits;

    PostState state;

    int views;
    int likes;

    Location location;
    String avatarUrl;
    List<String> imageNames;

    LocalDateTime createdAt;
    LocalDateTime expiredAt;

}