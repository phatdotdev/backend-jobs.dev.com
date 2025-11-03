package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotNull;
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
public class UpdateAwardRequest {
    @NotNull(message = "Award name must not be null")
    @Size(min = 1, max = 255, message = "Award name must be between 1 and 255 characters")
    String name;

    @NotNull(message = "Organization must not be null")
    @Size(min = 1, max = 255, message = "Organization must be between 1 and 255 characters")
    String organization;

    @PastOrPresent(message = "Received date must be in the past or present")
    LocalDate receivedDate;

    @Size(max = 255, message = "Achievement must not exceed 255 characters")
    String achievement;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
