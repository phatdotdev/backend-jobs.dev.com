package com.dev.job.dto.response.User;

import com.dev.job.dto.response.Expertise.ExpertiseResponse;
import com.dev.job.entity.specification.Expertise;
import com.dev.job.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertResponse{
    UUID id;
    String username;
    String email;
    String phone;
    List<ExpertiseResponse> expertises;
    String avatarUrl;
    String coverUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
