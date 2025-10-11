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

    @NotBlank
    @Size(max = 255)
    String name;

    @NotBlank
    @Size(max = 255)
    String issuer;

    @PastOrPresent
    LocalDate issueDate;
    LocalDate expirationDate;

    @Size(max = 100)
    String credentialId;

    String credentialUrl;

    @Size(max = 1000)
    String description;
}
