package com.dev.job.dto.response.Expertise;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertiseResponse {
    UUID id;
    String title;
    String field;
    String description;
    int yearsOfExperience;
}
