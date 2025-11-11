package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.review.CreateFeedbackRequest;
import com.dev.job.dto.request.review.CreateFeedbackReview;
import com.dev.job.dto.response.review.FeedbackRequestResponse;
import com.dev.job.dto.response.review.FeedbackReviewResponse;
import com.dev.job.service.ReviewService;
import static com.dev.job.utils.ResponseHelper.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/feedback")
public class ReviewController {

    ReviewService reviewService;

    // CREATE REQUEST
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<FeedbackRequestResponse>> createRequest(
            @RequestBody CreateFeedbackRequest dto, Authentication authentication
    ) {
        return ok(reviewService.createRequest(dto, UUID.fromString(authentication.getName())));
    }

    // CREATE REVIEW
    @PreAuthorize("hasRole('EXPERT')")
    @PostMapping("/request/{requestId}/review")
    public ResponseEntity<ApiResponse<FeedbackReviewResponse>> submitReview(
            @PathVariable UUID requestId,
            @RequestBody CreateFeedbackReview dto,
            Authentication authentication
    ) {
        return ok(reviewService.submitReview(requestId, UUID.fromString(authentication.getName()), dto));
    }

    // GET REQUEST BY ID
    @GetMapping("/request/{id}")
    public ResponseEntity<FeedbackRequestResponse> getRequestById(@PathVariable UUID id) {
        FeedbackRequestResponse response = reviewService.getRequestById(id);
        return ResponseEntity.ok(response);
    }

    // GET REQUEST BY RESUME
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<FeedbackRequestResponse>> getRequestsByResume(@PathVariable UUID resumeId) {
        List<FeedbackRequestResponse> responses = reviewService.getRequestsByResume(resumeId);
        return ResponseEntity.ok(responses);
    }

    // GET ALL REQUESTS
    @PreAuthorize("hasRole('EXPERT')")
    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<Page<FeedbackRequestResponse>>> getFeedbackRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<FeedbackRequestResponse> responses = reviewService.getActiveRequests(PageRequest.of(page, size));
        return ok(responses);
    }

    @PreAuthorize("hasRole('EXPERT')")
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<Page<FeedbackReviewResponse>>> getMyFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Page<FeedbackReviewResponse> responses = reviewService
                .getMyFeedbacks(UUID.fromString(authentication.getName()),page, size);
        return ok(responses);
    }

    @PreAuthorize("hasRole('EXPERT')")
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<FeedbackReviewResponse>> getMyFeedbackById(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return ok(reviewService.getFeedbackById(id, userId));
    }

}
