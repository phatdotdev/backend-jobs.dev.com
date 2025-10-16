package com.dev.job.dto.response.Resume;

import com.dev.job.entity.resource.Link;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AwardResponse {
    String id;
    String name;
    String organization;
    LocalDate receivedDate;
    String achievement;
    String description;
    Link link;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
