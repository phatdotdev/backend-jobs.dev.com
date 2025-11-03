package com.dev.job.dto.response.Resume;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeResponse {
    UUID id;

    String title;

    String firstname;
    String lastname;
    String phone;
    String email;
    String address;
    String gender;
    LocalDate dob;

    String introduction;
    String objectCareer;

    List<EducationResponse> educations;
    List<ExperienceResponse> experiences;
    List<SkillResponse> skills;
    List<CertificationResponse> certifications;
    List<AwardResponse> awards;
    List<ActivityResponse> activities;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
