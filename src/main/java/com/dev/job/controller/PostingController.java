package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Posting.CreateJobPostingRequest;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.service.PostingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/postings")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostingController {

    PostingService postingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>> getAllJobPostings(Authentication authentication) {
        UUID rId = UUID.fromString(authentication.getName());
        List<JobPostingResponse> postings = postingService.getAllJobPostings(rId);

        return ResponseEntity.ok(
                ApiResponse.<List<JobPostingResponse>>builder()
                        .data(postings)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobPostingResponse>> createJobPosting(Authentication authentication, @RequestBody  CreateJobPostingRequest request){
        UUID rId = UUID.fromString(authentication.getName());
        JobPostingResponse response = postingService.createJobPosting(request, rId);
        return ResponseEntity.ok(
                ApiResponse.<JobPostingResponse>builder()
                    .success(true)
                    .message("Create job posting successfully.")
                    .data(response)
                    .build()
        );
    }


}
