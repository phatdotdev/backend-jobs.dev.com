package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.AuthenticationRequest;
import com.dev.job.dto.request.User.CreateUserRequest;
import com.dev.job.dto.response.AuthenticationResponse;
import com.dev.job.dto.response.UserResponse;
import com.dev.job.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody CreateUserRequest request){
        return ResponseEntity
                .ok(ApiResponse
                        .<UserResponse>builder()
                        .success(true)
                        .message("User created successfully.")
                        .data(authenticationService.signup(request))
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.authenticate(request);
        ResponseCookie cookie = ResponseCookie
                .from("token", response.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse
                        .<AuthenticationResponse>builder()
                        .success(true)
                        .message("Authenticated successfully.")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasRole('JOBSEEKER')")
    @GetMapping("/job-seeker")
    public String jobseeker(){
        return "JOBSEEKER";
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter")
    public String recruiter(){
        return "RECRUITER";
    }
}
