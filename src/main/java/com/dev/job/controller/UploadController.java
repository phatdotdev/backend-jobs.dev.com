package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.entity.resource.Image;
import com.dev.job.service.UploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UploadController {

    UploadService uploadService;

    @PostMapping("/upload/avatar")
    public ResponseEntity<ApiResponse<Image>> handleUploadAvatar(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
            ApiResponse.<Image>builder()
                    .success(true)
                    .message("Upload avatar successfully")
                    .data(uploadService.uploadUserAvatar(file, userId))
                    .build()
        );
    }
    @PostMapping("/upload/background")
    public ResponseEntity<ApiResponse<Image>> handleUploadBackground(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<Image>builder()
                        .success(true)
                        .message("Upload background successfully")
                        .data(uploadService.uploadUserBackground(file, userId))
                        .build()
        );
    }
}
