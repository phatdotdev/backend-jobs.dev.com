package com.dev.job.dto.response.Expertise;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertiseResponse {
    String title;
    String field;
    String description;
    int yearsOfExperience;
}
