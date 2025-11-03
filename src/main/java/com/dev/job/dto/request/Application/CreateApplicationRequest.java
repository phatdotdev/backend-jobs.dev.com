package com.dev.job.dto.request.Application;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateApplicationRequest {
    UUID resumeId;
    UUID postId;
}
