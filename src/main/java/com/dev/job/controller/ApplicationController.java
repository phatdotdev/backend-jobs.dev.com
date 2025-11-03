package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Application.CreateApplicationRequest;
import com.dev.job.dto.response.Application.ApplicationResponse;
import com.dev.job.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
