package com.dev.job.dto.request.User;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    String oldPassword;
    @Size(min = 6, message = "Password must be at least 6 characters.")
    String newPassword;
}
