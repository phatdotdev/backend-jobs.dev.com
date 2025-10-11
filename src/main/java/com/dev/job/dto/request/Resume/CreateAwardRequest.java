package com.dev.job.dto.request.Resume;

import jakarta.validation.constraints.NotNull;
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
public class CreateAwardRequest {

    @NotNull
    @Size(min = 1, max = 255)
    String name;

    @NotNull
    @Size(min = 1, max = 255)
    String organization;

    @PastOrPresent
    LocalDate receivedDate;

    @Size(max = 255)
    String achievement;

    @Size(max = 1000)
    String description;

}
