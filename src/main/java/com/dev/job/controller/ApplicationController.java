package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Application.CreateApplicationRequest;
import static com.dev.job.utils.ResponseHelper.*;
import static com.dev.job.utils.ResponseHelper.ok;

import com.dev.job.dto.request.Application.UpdateApplicationStateRequest;
import com.dev.job.dto.response.Application.ApplicationResponse;
import com.dev.job.entity.application.ApplicationState;
import com.dev.job.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationController {

    ApplicationService applicationService;

    @PostMapping("/applications")
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyJob(@Valid @RequestBody CreateApplicationRequest request, Authentication authentication){
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
          ApiResponse.<ApplicationResponse>builder()
                  .success(true)
                  .message("Apply successfully.")
                  .data(applicationService.applyJob(request, jsId))
                  .build()
        );
    }

    @GetMapping("/applications/search")
    public ResponseEntity<ApiResponse<ApplicationResponse>> findApply(@RequestParam UUID postId, Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.searchApply(postId, userId));
    }

    @GetMapping("/applications/mine")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getMyApplications(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                    @RequestParam(required = false, defaultValue = "10") int size,
                                                                                    Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.getMyApplications(userId, page, size));
    }

    @GetMapping("/applications/post/{postId}")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getJobPostApplications(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                    @RequestParam(required = false, defaultValue = "10") int size,
                                                                                         @RequestParam(required = false) ApplicationState state,
                                                                                    @PathVariable UUID postId){
        return ok(applicationService.getJobPostingApplication(postId, state, page, size));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> findAllApplies(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                 @RequestParam(required = false, defaultValue = "10") int size){
        return ok(applicationService.getAllApplication(page, size));
    }

    @PutMapping("/applications/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateApplicationStatus(@PathVariable UUID id,
                                                                                    @RequestBody UpdateApplicationStateRequest request,
                                                                                    Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.updateApplicationState(id, request, userId));
    }

}
