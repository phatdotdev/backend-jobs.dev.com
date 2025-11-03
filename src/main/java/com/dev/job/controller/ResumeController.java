package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Resume.*;
import com.dev.job.dto.response.Resume.*;
import com.dev.job.service.ResumeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('JOBSEEKER')")
@RequestMapping("/")
public class ResumeController {

    ResumeService resumeService;

    /********** RESUME ROUTES ***********/

    @GetMapping("/resumes")
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getAllResumes(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
          ApiResponse.<List<ResumeResponse>>builder()
                  .success(true)
                  .data(resumeService.getAllResumes(jsId))
                  .build()
        );
    }

    @PostMapping("/resumes")
    public ResponseEntity<ApiResponse<ResumeResponse>> createResume(@Valid @RequestBody CreateResumeRequest request, Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<ResumeResponse>builder()
                        .success(true)
                        .message("Create resume successfully.")
                        .data(resumeService.createResume(request, jsId))
                        .build()
        );
    }

    @GetMapping("/resumes/{resumeId}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResume(@PathVariable String resumeId, Authentication authentication){
        UUID rId = UUID.fromString(resumeId);
        return ResponseEntity.ok(
                ApiResponse.<ResumeResponse>builder()
                        .success(true)
                        .message("Create resume successfully.")
                        .data(resumeService.getResume(rId))
                        .build()
        );
    }

    @PutMapping("/resumes/{resumeId}")
    public ResponseEntity<ApiResponse<ResumeResponse>> updateResume(@Valid @RequestBody UpdateResumeRequest request ,@PathVariable String resumeId, Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        UUID rId = UUID.fromString(resumeId);
        return ResponseEntity.ok(
                ApiResponse.<ResumeResponse>builder()
                        .success(true)
                        .message("Create resume successfully.")
                        .data(resumeService.updateResume(request, rId, jsId))
                        .build()
        );
    }

    /*********** EDUCATION ROUTES **********/

    @GetMapping("/educations")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllEducations(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @PostMapping("/educations")
    public ResponseEntity<ApiResponse<EducationResponse>> addEducation(Authentication authentication, @RequestBody @Valid CreateEducationRequest request){
        UUID id = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
            ApiResponse.<EducationResponse>builder()
                    .success(true)
                    .message("Education saved.")
                    .data(resumeService.addEducation(request, id))
                    .build()
        );
    }

    @PutMapping("/educations/{educationId}")
    public ResponseEntity<ApiResponse<EducationResponse>> updateEducation(Authentication authentication, @RequestBody @Valid UpdateEducationRequest request, @PathVariable UUID educationId){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<EducationResponse>builder()
                        .success(true)
                        .data(resumeService.updateEducation(request, educationId, jsId))
                        .build()
        );
    }

    @DeleteMapping("/educations/{educationId}")
    public ResponseEntity<ApiResponse<Void>> deleteEducation(Authentication authentication, @PathVariable UUID educationId){
        UUID jsId = UUID.fromString(authentication.getName());
        resumeService.deleteEducation(educationId, jsId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Education deleted.")
                        .build()
        );
    }

    /********** EXPERIENCE ROUTES **********/

    @GetMapping("/experiences")
    public ResponseEntity<ApiResponse<List<ExperienceResponse>>> getAllExperiences(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<ExperienceResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllExperiences(jsId))
                        .build()
                );
    }

    @PostMapping("/experiences")
    public ResponseEntity<ApiResponse<ExperienceResponse>> addExperience(Authentication authentication, @RequestBody @Valid CreateExperienceRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<ExperienceResponse>builder()
                        .success(true)
                        .data(resumeService.addExperience(request, jsId))
                        .build()
                );
    }

    @PutMapping("/experiences/{experienceId}")
    public ResponseEntity<ApiResponse<ExperienceResponse>> updateExperience(Authentication authentication, @RequestBody @Valid UpdateExperienceRequest request, @PathVariable UUID experienceId){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<ExperienceResponse>builder()
                        .success(true)
                        .data(resumeService.updateExperience(request, experienceId, jsId))
                        .build()
        );
    }

    @DeleteMapping("/experiences/{experienceId}")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(Authentication authentication, @PathVariable UUID experienceId){
        UUID jsId = UUID.fromString(authentication.getName());
        resumeService.deleteExperience(experienceId, jsId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Experience deleted.")
                        .build()
        );
    }

    /********** CERTIFICATION ROUTES **********/

    @GetMapping("/certifications")
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getAllCertifications(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<CertificationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllCertifications(jsId))
                        .build()
                );
    }

    @PostMapping("/certifications")
    public ResponseEntity<ApiResponse<CertificationResponse>> addCertification(Authentication authentication, @RequestBody @Valid CreateCertificationRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<CertificationResponse>builder()
                        .success(true)
                        .data(resumeService.addCertification(request, jsId))
                        .build()
                );
    }

    @PutMapping("/certifications/{certificationId}")
    public ResponseEntity<ApiResponse<CertificationResponse>> updateCertification(Authentication authentication, @RequestBody @Valid UpdateCertificationRequest request, @PathVariable UUID certificationId){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<CertificationResponse>builder()
                        .success(true)
                        .data(resumeService.updateCertification(request, certificationId, jsId))
                        .build()
        );
    }

    @DeleteMapping("/certifications/{certificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(Authentication authentication, @PathVariable UUID certificationId){
        UUID jsId = UUID.fromString(authentication.getName());
        resumeService.deleteCertification(certificationId, jsId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Certification deleted.")
                        .build()
        );
    }

    /********** SKILL ROUTES **********/

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getAllSkills(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<SkillResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllSkills(jsId))
                        .build()
                );
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<SkillResponse>> addSkill(Authentication authentication, @RequestBody @Valid CreateSkillRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<SkillResponse>builder()
                        .success(true)
                        .data(resumeService.addSkill(request, jsId))
                        .build()
                );
    }

    @PutMapping("/skills/{skillId}")
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(Authentication authentication, @RequestBody @Valid UpdateSkillRequest request, @PathVariable UUID skillId){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<SkillResponse>builder()
                        .success(true)
                        .data(resumeService.updateSkill(request, skillId, jsId))
                        .build()
        );
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(Authentication authentication, @PathVariable UUID skillId){
        UUID jsId = UUID.fromString(authentication.getName());
        resumeService.deleteSkill(skillId, jsId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Skill deleted.")
                        .build()
        );
    }

    /********** PROJECT ROTES **********/

    @GetMapping("/projects")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<ProjectResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllProjects(jsId))
                        .build()
                );
    }

    @PostMapping("/projects")
    public ResponseEntity<ApiResponse<ProjectResponse>> addProject(Authentication authentication, @RequestBody @Valid CreateProjectRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<ProjectResponse>builder()
                        .success(true)
                        .data(resumeService.addProject(request, jsId))
                        .build()
                );
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(Authentication authentication, @RequestBody @Valid UpdateProjectRequest request, @PathVariable UUID projectId){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<ProjectResponse>builder()
                        .success(true)
                        .data(resumeService.updateProject(request, projectId, jsId))
                        .build()
        );
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(Authentication authentication, @PathVariable UUID projectId){
        UUID jsId = UUID.fromString(authentication.getName());
        resumeService.deleteProject(projectId, jsId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Project deleted.")
                        .build()
        );
    }

    /********** ACTIVITY ROUTES **********/

    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<List<ActivityResponse>>> getAllActivities(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<ActivityResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllActivities(jsId))
                        .build()
                );
    }

    @PostMapping("/activities")
    public ResponseEntity<ApiResponse<ActivityResponse>> addActivity(Authentication authentication, @RequestBody @Valid CreateActivityRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<ActivityResponse>builder()
                        .success(true)
                        .data(resumeService.addActivity(request, jsId))
                        .build()
                );
    }

    @PutMapping("/activities/{activityId}")
    public ResponseEntity<ApiResponse<ActivityResponse>> updateActivity(Authentication authentication, @RequestBody @Valid UpdateActivityRequest request, @PathVariable UUID activityId){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<ActivityResponse>builder()
                        .success(true)
                        .data(resumeService.updateActivity(request, activityId, jsId))
                        .build()
        );
    }

    @DeleteMapping("/activities/{activityId}")
    public ResponseEntity<ApiResponse<Void>> deleteActivity(Authentication authentication, @PathVariable UUID activityId){
        UUID jsId = UUID.fromString(authentication.getName());
        resumeService.deleteActivity(activityId, jsId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Activity deleted.")
                        .build()
        );
    }

    /********** AWARD ROUTES **********/

    @GetMapping("/awards")
    public ResponseEntity<ApiResponse<List<AwardResponse>>> getAllAwards(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<AwardResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllAwards(jsId))
                        .build()
                );
    }

    @PostMapping("/awards")
    public ResponseEntity<ApiResponse<AwardResponse>> addAward(Authentication authentication, @RequestBody @Valid CreateAwardRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<AwardResponse>builder()
                        .success(true)
                        .data(resumeService.addAward(request, jsId))
                        .build()
                );
    }

    @PutMapping("/awards/{awardId}")
    public ResponseEntity<ApiResponse<AwardResponse>> updateAward(Authentication authentication, @RequestBody @Valid UpdateAwardRequest request, @PathVariable UUID awardId){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<AwardResponse>builder()
                        .success(true)
                        .data(resumeService.updateAward(request, awardId, jsId))
                        .build()
        );
    }

    @DeleteMapping("/awards/{awardId}")
    public ResponseEntity<ApiResponse<Void>> deleteAward(Authentication authentication, @PathVariable UUID awardId){
        UUID jsId = UUID.fromString(authentication.getName());
        resumeService.deleteAward(awardId, jsId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Award deleted.")
                        .build()
        );
    }

}
