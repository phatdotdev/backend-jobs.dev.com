package com.dev.job.dto.response.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    UUID id;
    String username;
    String email;
    String password;
    String role;
    String status;
    String avatarUrl;
    String coverUrl;
    LocalDate createdAt;
    LocalDate updatedAt;
}
