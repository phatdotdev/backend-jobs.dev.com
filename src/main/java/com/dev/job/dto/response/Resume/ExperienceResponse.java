package com.dev.job.dto.response.Resume;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperienceResponse {
    String id;
    String companyName;
    String position;
    LocalDate startDate;
    LocalDate endDate;
    String description;
}
