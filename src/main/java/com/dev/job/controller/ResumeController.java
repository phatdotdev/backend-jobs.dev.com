package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Resume.CreateEducationRequest;
import com.dev.job.dto.response.Resume.EducationResponse;
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

    @GetMapping("/experiences")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllExperiences(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @PostMapping("/experiences")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> addExperience(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @GetMapping("/certifications")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllCertifications(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @PostMapping("/certifications")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> addCertification(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllSkills(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> addSkill(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @GetMapping("/projects")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllProjects(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @PostMapping("/projects")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> addProject(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllActivities(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @PostMapping("/activities")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> addActivity(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @GetMapping("/awards")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getAllAwards(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }

    @PostMapping("/awards")
    public ResponseEntity<ApiResponse<List<EducationResponse>>> addAward(Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.<List<EducationResponse>>builder()
                        .success(true)
                        .data(resumeService.getAllEducations(jsId))
                        .build()
                );
    }


}
