package com.dev.job.dto.response.Resume;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResumeResponse {


    List<EducationResponse> educations;
    List<ExperienceResponse> experiences;
    List<SkillResponse> skills;
    List<CertificationResponse> certifications;
    List<AwardResponse> awards;
    List<ActivityResponse> activities;

}
