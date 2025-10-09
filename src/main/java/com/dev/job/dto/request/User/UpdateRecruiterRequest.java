package com.dev.job.dto.request.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRecruiterRequest {
    String username;
    String name;
    String description;
    String phone;
    String address;
}
