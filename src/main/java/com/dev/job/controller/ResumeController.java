package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Resume.*;
import com.dev.job.dto.response.Resume.*;
import com.dev.job.service.ResumeService;
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
    public ResponseEntity<ApiResponse<EducationResponse>> addEducation(Authentication authentication, @RequestBody CreateEducationRequest request){
        UUID id = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
            ApiResponse.<EducationResponse>builder()
                    .success(true)
                    .message("Education saved.")
                    .data(resumeService.addEducation(request, id))
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
    public ResponseEntity<ApiResponse<ExperienceResponse>> addExperience(Authentication authentication, @RequestBody CreateExperienceRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<ExperienceResponse>builder()
                        .success(true)
                        .data(resumeService.addExperience(request, jsId))
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
    public ResponseEntity<ApiResponse<CertificationResponse>> addCertification(Authentication authentication, @RequestBody CreateCertificationRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<CertificationResponse>builder()
                        .success(true)
                        .data(resumeService.addCertification(request, jsId))
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
    public ResponseEntity<ApiResponse<SkillResponse>> addSkill(Authentication authentication, @RequestBody CreateSkillRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<SkillResponse>builder()
                        .success(true)
                        .data(resumeService.addSkill(request, jsId))
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
    public ResponseEntity<ApiResponse<ProjectResponse>> addProject(Authentication authentication, @RequestBody CreateProjectRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<ProjectResponse>builder()
                        .success(true)
                        .data(resumeService.addProject(request, jsId))
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
    public ResponseEntity<ApiResponse<ActivityResponse>> addActivity(Authentication authentication, @RequestBody CreateActivityRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<ActivityResponse>builder()
                        .success(true)
                        .data(resumeService.addActivity(request, jsId))
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
    public ResponseEntity<ApiResponse<AwardResponse>> addAward(Authentication authentication, @RequestBody CreateAwardRequest request){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<AwardResponse>builder()
                        .success(true)
                        .data(resumeService.addAward(request, jsId))
                        .build()
                );
    }

}
