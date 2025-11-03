package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.AuthenticationRequest;
import com.dev.job.dto.request.User.CreateUserRequest;
import com.dev.job.dto.response.AuthenticationResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.dev.job.utils.ResponseHelper.*;

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
        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", response.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", response.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
                .body(ApiResponse
                        .<AuthenticationResponse>builder()
                        .success(true)
                        .message("Authenticated successfully.")
                        .data(response)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deleteAccessCookie = ResponseCookie
                .from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        ResponseCookie deleteRefreshCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString(), deleteRefreshCookie.toString())
                .body("Logout successfully.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@CookieValue("refresh_token") String refreshToken) {
        String accessToken = authenticationService.refresh(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", accessCookie.toString())
                .body(ApiResponse
                        .<String>builder()
                        .success(true)
                        .data("Access token refreshed.").build());
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<Boolean>> introspect(@CookieValue("access_token") String accessToken) {
        return ok(authenticationService.introspect(accessToken));
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
