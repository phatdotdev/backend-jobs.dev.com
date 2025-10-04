package com.dev.job.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    Long id;
    String email;
    String status;
    String role;
    LocalDate create_at;
    LocalDate update_at;
    LocalDate update_by;
}
