package com.dev.job.dto.response.Resume;

import com.dev.job.entity.resource.Link;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
}
