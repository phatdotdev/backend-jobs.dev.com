package com.dev.job.dto.response.Application;

import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.dto.response.Resume.ResumeResponse;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.entity.application.ApplicationState;
import com.dev.job.entity.user.JobSeeker;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {
    UUID id;
    ResumeResponse resume;
    JobPostingResponse post;
    ApplicationState state;
    LocalDateTime appliedAt;
    LocalDateTime updatedAt;
}
