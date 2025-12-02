package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.service.GeminiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dev.job.utils.ResponseHelper.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GeminiController {

    GeminiService geminiService;

    @PostMapping("{resumeId}/job-suggestions")
    public ResponseEntity<ApiResponse<Object>> suggestJobPosting(@PathVariable UUID resumeId) {
        return ok(geminiService.suggestTop5Jobs(resumeId));
    }

    @PostMapping("{postId}/candidate-suggestions")
    public ResponseEntity<ApiResponse<Object>> suggestCandidates(@PathVariable UUID postId){
        return ok(geminiService.suggestTop5Candidates(postId));
    }
}
