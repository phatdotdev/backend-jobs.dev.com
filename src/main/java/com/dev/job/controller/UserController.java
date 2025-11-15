package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.User.*;

import com.dev.job.dto.response.User.ExpertResponse;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.dto.response.User.RecruiterResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.resource.Image;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.entity.user.UserStatus;
import com.dev.job.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.UUID;

import static com.dev.job.utils.ResponseHelper.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    /********** USER IMAGE **********/

    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatar(Authentication authentication) throws IOException {
        UUID userId = UUID.fromString(authentication.getName());
        Image avatar = userService.getUserAvatar(userId);

        File file = new File(avatar.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getFileType()))
                .body(bytes);
    }

    @GetMapping("/background")
    public ResponseEntity<byte[]> getBackground(Authentication authentication) throws IOException {
        UUID userId = UUID.fromString(authentication.getName());
        Image background = userService.getUserBackground(userId);

        File file = new File(background.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Files.readAllBytes(file.toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(background.getFileType()))
                .body(bytes);
    }

    @GetMapping("/avatar/{id}")
    public ResponseEntity<byte[]> getUserAvatar(UUID id) throws IOException {
        Image background = userService.getUserAvatar(id);

        File file = new File(background.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Files.readAllBytes(file.toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(background.getFileType()))
                .body(bytes);
    }

    @GetMapping("/background/{id}")
    public ResponseEntity<byte[]> getUserBackground(UUID id) throws IOException {
        Image background = userService.getUserBackground(id);

        File file = new File(background.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Files.readAllBytes(file.toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(background.getFileType()))
                .body(bytes);
    }


    /*********** COMPANIES SEARCH **********/
    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<Page<RecruiterResponse>>> searchCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String companyName){
            return ok(userService.getRecruiters(page, size, null, null, null, companyName));
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<ApiResponse<RecruiterResponse>> getCompanyById(@PathVariable UUID id){
        return ok(userService.getRecruiterById(id));
    }

    /*********** USER MANAGEMENT **********/

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        return ok(userService.getUsers(page, size, username, email, role, status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<String>> inactiveUser(@PathVariable UUID userId,
            @RequestParam UserStatus status) {
        boolean success = userService.updateUserStatus(userId, status);

        if (success) {
            return ok("User updated successfully.");
        } else {
            return notFound("User not found.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/job-seekers")
    public ResponseEntity<ApiResponse<Page<JobSeekerResponse>>> getJobSeekers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        return ok(userService.getJobSeekers(page, size, username, email, phone));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/recruiters")
    public ResponseEntity<ApiResponse<Page<RecruiterResponse>>> getRecruiters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String companyName) {
        return ok(userService.getRecruiters(page, size, username, email, phone, companyName));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/experts")
    public ResponseEntity<ApiResponse<Page<ExpertResponse>>> getExperts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        return ok(userService.getExperts(page, size, username, email));
    }

    /*********** CREATE USER *************/

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/job-seekers")
    public ResponseEntity<ApiResponse<JobSeekerResponse>> createJobSeeker(@RequestBody CreateJobSeekerRequest request) {
        return created(userService.createJobSeeker(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/recruiters")
    public ResponseEntity<ApiResponse<RecruiterResponse>> createExpert(@RequestBody CreateRecruiterRequest request) {
        return created(userService.createRecruiter(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/experts")
    public ResponseEntity<ApiResponse<ExpertResponse>> createExpert(@RequestBody CreateExpertRequest request) {
        return created(userService.createExpert(request));
    }

    /* */

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID userId) {
        boolean success = userService.deleteUser(userId);

        if (success) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("User not found or already deleted.")
                            .build());
        }
    }

    /*********** USER DETAILS **********/

    @GetMapping("/info")
    ResponseEntity<ApiResponse<UserResponse>> getAccountInfo(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ok(userService.getUserInfo(userId));
    }

    @PutMapping("/password")
    ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        userService.changePassword(request, userId);
        return ok("Password updated.");
    }

    /*********** USER INFO ***********/

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserResponse> userPage;
        if (search == null) {
            userPage = userService.getAllUsers(pageable);
        } else {
            userPage = userService.searchUsers(search, pageable);
        }
        return ResponseEntity.ok(
                ApiResponse.<Page<UserResponse>>builder()
                        .success(true)
                        .data(userPage)
                        .build());
    }

    @PreAuthorize("hasRole('JOBSEEKER')")
    @GetMapping("/job-seeker/profile")
    public ResponseEntity<ApiResponse<JobSeekerResponse>> getJobSeekerProfile(Authentication authentication) {
        UUID id = UUID.fromString(authentication.getName());
        return ok(userService.getJobSeekerProfile(id));
    }

    @PreAuthorize("hasRole('JOBSEEKER')")
    @PutMapping("/job-seeker/profile")
    public ResponseEntity<ApiResponse<JobSeekerResponse>> updateJobSeekerProfile(Authentication authentication,
            @RequestBody UpdateJobSeekerRequest request) {
        UUID id = UUID.fromString(authentication.getName());
        return ok(userService.updateJobSeekerProfile(request, id));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter/profile")
    public ResponseEntity<ApiResponse<RecruiterResponse>> getRecruiterProfile(Authentication authentication) {
        UUID id = UUID.fromString(authentication.getName());
        return ok(userService.getRecruiterProfile(id));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/recruiter/profile")
    public ResponseEntity<ApiResponse<RecruiterResponse>> updateRecruiterProfile(Authentication authentication,
            @RequestBody UpdateRecruiterRequest request) {
        UUID id = UUID.fromString(authentication.getName());
        return ok(userService.updateRecruiterProfile(request, id));
    }

    @PreAuthorize("hasRole('EXPERT')")
    @GetMapping("/expert/profile")
    public ResponseEntity<ApiResponse<ExpertResponse>> getExpertProfile(Authentication authentication) {
        UUID id = UUID.fromString(authentication.getName());
        return ok(userService.getExpertProfile(id));
    }

    @PreAuthorize("hasRole('EXPERT')")
    @PutMapping("/expert/profile")
    public ResponseEntity<ApiResponse<ExpertResponse>> updateExpertProfile(Authentication authentication,
                                                                                 @RequestBody UpdateExpertRequest request) {
        UUID id = UUID.fromString(authentication.getName());
        return ok(userService.updateExpertProfile(request, id));
    }

    // GET USERS BY ID
    @GetMapping("/recruiter/{id}")
    ResponseEntity<ApiResponse<RecruiterResponse>> getRecruiterById(@PathVariable UUID id){
        return ok(userService.getRecruiterById(id));
    }
}
