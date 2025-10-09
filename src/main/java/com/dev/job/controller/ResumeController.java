package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Resume.CreateEducationRequest;
import com.dev.job.dto.response.Resume.EducationResponse;
import com.dev.job.service.ResumeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
}
