package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendationController {


    private final RecommendationService recommendationService;

    @GetMapping("/postings")
    public ResponseEntity<ApiResponse<List<JobPostingResponse>>> getRecommendedJobs(
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<List<JobPostingResponse>>builder()
                        .success(true)
                        .data(recommendationService.getRecommendedJobs(userId))
                        .build()
        );
    }

    @GetMapping("/recruiters")
    public ResponseEntity<ApiResponse<List<Recruiter>>> getRecommendedCompanies(
            Authentication authentication
    ) {

        UUID userId = UUID.fromString(authentication.getName());

        List<Recruiter> results = recommendationService.getRecommendedCompanies(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<Recruiter>>builder()
                        .success(true)
                        .message("Lấy danh sách công ty phù hợp thành công")
                        .data(results)
                        .build()
        );
    }


}