package com.dev.job.dto.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateJobSeekerRequest {
    @Length(min = 3, message = "Username must be at least 3 characters.")
    @NotNull(message = "Username is required.")
    String username;

    @Email(message = "Invalid email.")
    @NotNull(message = "Email is required.")
    String email;

    @Length(min = 8, message = "Password must be at least 8 characters.")
    String password;

    String role;

    String firstname;
    String lastname;
    String phone;
    String address;
    String gender;
    LocalDate dob;
}
