package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateEducationRequest {

    @NotBlank
    @Size(max = 255)
    String schoolName;

    @NotBlank
    @Size(max = 255)
    String degree;
    String major;

    @PastOrPresent
    LocalDate startDate;
    LocalDate endDate;

    @DecimalMax(value = "4.0")
    @DecimalMin(value = "0.0")
    Double grade;

    @Size(max = 1000)
    String description;
}
