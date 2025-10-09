package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.User.UpdateJobSeekerRequest;
import com.dev.job.dto.request.User.UpdateRecruiterRequest;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.dto.response.User.RecruiterResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.user.User;
import com.dev.job.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(required = false) String search){
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserResponse> userPage;
        if(search == null) {
            userPage = userService.getAllUsers(pageable);
        } else {
            userPage = userService.searchUsers(search, pageable);
        }
        return ResponseEntity.ok(
            ApiResponse.<Page<UserResponse>>builder()
                    .success(true)
                    .data(userPage)
                    .build()
        );
    }

    @PreAuthorize("hasRole('JOBSEEKER')")
    @GetMapping("/job-seeker/profile")
    ResponseEntity<ApiResponse<JobSeekerResponse>> getJobSeekerProfile(Authentication authentication){
        UUID id = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse
                        .<JobSeekerResponse>builder()
                        .success(true)
                        .data(userService.getJobSeekerProfile(id))
                        .build());
    }

    @PreAuthorize("hasRole('JOBSEEKER')")
    @PutMapping("/job-seeker/profile")
    ResponseEntity<ApiResponse<JobSeekerResponse>> updateJobSeekerProfile(Authentication authentication, @RequestBody UpdateJobSeekerRequest request){
        UUID id = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
            ApiResponse
                .<JobSeekerResponse>builder()
                .success(true)
                .message("Update job seeker profile successfully.")
                .data(userService.updateJobSeekerProfile(request, id))
                .build());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter/profile")
    public ResponseEntity<ApiResponse<RecruiterResponse>> getRecruiterProfile(Authentication authentication){
        UUID id = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
            ApiResponse.<RecruiterResponse>builder()
                .success(true)
                .data(userService.getRecruiterProfile(id))
                .build()
        );
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/recruiter/profile")
    public ResponseEntity<ApiResponse<RecruiterResponse>> updateRecruiterProfile(Authentication authentication, @RequestBody UpdateRecruiterRequest request){
        UUID id = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<RecruiterResponse>builder()
                        .success(true)
                        .data(userService.updateRecruiterProfile(request, id))
                        .build()
        );
    }
}
