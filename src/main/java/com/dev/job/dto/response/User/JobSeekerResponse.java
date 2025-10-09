package com.dev.job.dto.response.User;

import com.dev.job.entity.user.User;
import com.dev.job.entity.user.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobSeekerResponse {
    UUID id;
    String username;
    String email;
    UserRole role;
    String status;
    String firstname;
    String lastname;
    String phone;
    String address;
    String gender;
    LocalDate dob;
    LocalDate createdAt;
    LocalDate updatedAt;
}
