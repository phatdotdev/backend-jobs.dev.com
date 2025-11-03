package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCertificationRequest {

    @NotBlank(message = "Certification name must not be blank")
    @Size(max = 255, message = "Certification name must not exceed 255 characters")
    String name;

    @NotBlank(message = "Issuer must not be blank")
    @Size(max = 255, message = "Issuer must not exceed 255 characters")
    String issuer;

    @PastOrPresent(message = "Issue date must be in the past or present")
    LocalDate issueDate;

    LocalDate expirationDate;

    @Size(max = 100, message = "Credential ID must not exceed 100 characters")
    String credentialId;

    @Size(max = 255, message = "Credential URL must not exceed 255 characters")
    String credentialUrl;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
