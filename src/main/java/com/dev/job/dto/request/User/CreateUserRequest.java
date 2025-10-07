package com.dev.job.dto.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class CreateUserRequest {
    @Length(min = 3, message = "Username must be at least 3 characters.")
    @NotNull(message = "Username is required.")
    String username;

    @Email(message = "Invalid email.")
    @NotNull(message = "Email is required.")
    String email;

    @Length(min = 8, message = "Password must be at least 8 characters.")
    String password;

    @Pattern(regexp = "^(RECRUITER|JOBSEEKER)", message = "Role must be either 'recruiter' or 'job_seeker'")
    String role;
}
