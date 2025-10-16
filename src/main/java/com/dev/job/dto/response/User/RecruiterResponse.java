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
public class RecruiterResponse {
    UUID id;
    String username;
    String email;
    UserRole role;
    String status;
    String companyName;
    String description;
    String phone;
    String address;
    boolean verified;
    LocalDate createdAt;
    LocalDate updatedAt;
}
