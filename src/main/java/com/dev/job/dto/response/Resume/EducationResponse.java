package com.dev.job.dto.response.Resume;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationResponse {
    String id;
    String schoolName;
    String degree;
    String major;
    LocalDate startDate;
    LocalDate endDate;
    Double grade;
    String description;
}
