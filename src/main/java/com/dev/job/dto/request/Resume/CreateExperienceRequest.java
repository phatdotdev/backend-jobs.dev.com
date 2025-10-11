package com.dev.job.dto.request.Resume;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateExperienceRequest {

    @NotBlank
    @Size(max = 255)
    String companyName;

    @NotBlank
    @Size(max = 255)
    String position;

    @PastOrPresent
    LocalDate startDate;
    LocalDate endDate;

    @Size(max = 1000)
    String description;
}
