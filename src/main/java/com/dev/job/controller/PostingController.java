package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Posting.CreateJobPostingRequest;
import com.dev.job.dto.request.Posting.UpdateJobPostingRequest;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.entity.posting.JobType;
import com.dev.job.entity.posting.PostState;
import com.dev.job.service.PostingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import static com.dev.job.utils.ResponseHelper.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/postings")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostingController {

    PostingService postingService;
    ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<JobPostingResponse>> createJobPosting(
            Authentication authentication,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles
    ) throws IOException {
        CreateJobPostingRequest request = objectMapper.readValue(dataJson, CreateJobPostingRequest.class);
        UUID recruiterId = UUID.fromString(authentication.getName());
        JobPostingResponse response = postingService.createJobPosting(request, imageFiles, recruiterId);

        return ResponseEntity.ok(ApiResponse.<JobPostingResponse>builder()
                .success(true)
                .message("Tạo job thành công.")
                .data(response)
                .build());
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<JobPostingResponse>>> searchJobPostings(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) BigDecimal minSalary,
        @RequestParam(required = false) BigDecimal maxSalary,
        @RequestParam(required = false) UUID locationId,
        @RequestParam(required = false) JobType type,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Page<JobPostingResponse> results = postingService.searchJobPostings(keyword, minSalary, maxSalary, locationId, type, page, size);
        return ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> getJobPostById(@PathVariable UUID id){
        return ok(postingService.getJobPosting(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobPostingResponse>>> getAllJobPostings(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) UUID locationId,
            @RequestParam(required = false) JobType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<JobPostingResponse> results = postingService.getAllJobPostings(keyword, minSalary, maxSalary, locationId, type, page, size);
        return ResponseEntity.ok(ApiResponse
                .<Page<JobPostingResponse>>builder()
                .data(results)
                .build());
    }

    // GET MY POSTS
    @GetMapping("/mine")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<Page<JobPostingResponse>>> getMineJobPostings(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UUID rId = UUID.fromString(authentication.getName());
        Page<JobPostingResponse> postings = postingService.getMineJobPostings(rId, page, size);
        return ok(postings);
    }


    @PutMapping("/{postingId}/state")
    public ResponseEntity<ApiResponse<JobPostingResponse>> updateJobPostingState(Authentication authentication,
                                                                                 @RequestParam PostState state,
                                                                                 @PathVariable UUID postingId){
        UUID rId = UUID.fromString(authentication.getName());
        return ok(postingService.updateJobPostingState(state, postingId, rId));
    }

    @PutMapping("/{postingId}")
    public ResponseEntity<ApiResponse<JobPostingResponse>> updateJobPosting(
            Authentication authentication,
            @PathVariable UUID postingId,
            @RequestPart("data") String dataJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles
    ) throws IOException {
        UUID rId = UUID.fromString(authentication.getName());
        UpdateJobPostingRequest request = objectMapper.readValue(dataJson, UpdateJobPostingRequest.class);
        return ok(postingService.updateJobPosting(request, postingId, rId, imageFiles));
    }

    @NonFinal
    @Value("${file.upload-dir}")
    String uploadDir;

    @GetMapping("/{postId}/images/{filename}")
    public ResponseEntity<byte[]> getPostImageBytes(
            @PathVariable UUID postId,
            @PathVariable String filename
    ) throws IOException {
        Path imagePath = Paths.get(uploadDir, "posts", postId.toString(), filename);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(imagePath);
        String contentType = Files.probeContentType(imagePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .body(imageBytes);
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> deletePostById(Authentication authentication, @PathVariable UUID postId){
        UUID userId = UUID.fromString(authentication.getName());
        postingService.deleteJobPosting(postId, userId);
        return ok("Delete post successfully.");
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<String>> likePost(@PathVariable UUID postId, Authentication authentication) {
        postingService.toggleLike(UUID.fromString(authentication.getName()), postId);
        return ok("Liked/Unliked!");
    }

    @PostMapping("/{postId}/view")
    public ResponseEntity<ApiResponse<String>> viewPost(@PathVariable UUID postId, Authentication authentication) {
        postingService.markAsViewed(UUID.fromString(authentication.getName()), postId);
        return ok("View!");
    }


}
