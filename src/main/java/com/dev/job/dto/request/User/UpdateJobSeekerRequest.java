package com.dev.job.dto.request.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateJobSeekerRequest {
    String username;
    String firstname;
    String lastname;
    String phone;
    String address;
    String gender;
    LocalDate dob;
}
