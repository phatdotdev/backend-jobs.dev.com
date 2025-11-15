package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;

import static com.dev.job.utils.ResponseHelper.ok;

import com.dev.job.dto.request.Application.UpdateApplicationStateRequest;
import com.dev.job.dto.response.Application.ApplicationResponse;
import com.dev.job.entity.application.ApplicationState;
import com.dev.job.service.ApplicationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/applications")
public class ApplicationController {

    ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyJob(@RequestParam("postId") UUID postId,
                                                                     @RequestParam("resumeId") UUID resumeId,
                                                                     @RequestParam(value = "documents", required = false) List<MultipartFile> files,
                                                                     Authentication authentication) throws IOException {
        UUID jsId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
          ApiResponse.<ApplicationResponse>builder()
                  .success(true)
                  .message("Apply successfully.")
                  .data(applicationService.applyJob(postId, resumeId, files, jsId))
                  .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ApplicationResponse>> findApply(@RequestParam UUID postId, Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.searchApply(postId, userId));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getMyApplications(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                    @RequestParam(required = false, defaultValue = "10") int size,
                                                                                    Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.getMyApplications(userId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplicationById(@PathVariable UUID id,
                                                                                    Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.getApplicationById(id, UUID.fromString(authentication.getName())));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getJobPostApplications(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                    @RequestParam(required = false, defaultValue = "10") int size,
                                                                                         @RequestParam(required = false) ApplicationState state,
                                                                                    @PathVariable UUID postId){
        return ok(applicationService.getJobPostingApplication(postId, state, page, size));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> findAllApplies(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                 @RequestParam(required = false, defaultValue = "10") int size){
        return ok(applicationService.getAllApplication(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateApplicationStatus(@PathVariable UUID id,
                                                                                    @RequestBody UpdateApplicationStateRequest request,
                                                                                    Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.updateApplicationState(id, request, userId));
    }

    @PutMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateDocuments(@PathVariable UUID id,
                                                                            @RequestParam(name = "documents", required = false) List<MultipartFile> files,
                                                                            Authentication authentication) throws IOException {
        UUID userId = UUID.fromString(authentication.getName());
        return ok(applicationService.updateDocuments(id, files, userId));
    }

}
