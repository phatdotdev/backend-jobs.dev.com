package com.dev.job.entity.user;

import com.dev.job.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "job_seekers")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobSeeker extends User {
    String firstname;
    String lastname;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    Gender gender;

    LocalDate dob;
    String hobbies;
    String careerObject;
}
