package com.dev.job.entity.resume;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "experience")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Certification {
    UUID id;
    String name;
    String issuer;
    LocalDate issueDate;
    LocalDate expirationDate;
    String credentialId;
    String credentialUrl;
    String description;
}
