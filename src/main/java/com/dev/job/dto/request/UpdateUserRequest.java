package com.dev.job.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    Long id;
    String email;
    String status;
    String role;
    LocalDate create_at;
    LocalDate update_at;
    LocalDate update_by;
}
