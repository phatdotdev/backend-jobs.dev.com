package com.dev.job.entity.user;

import com.dev.job.entity.resume.Education;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "job_seeker")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class JobSeeker extends User{
    String firstname;
    String lastname;
    String phone;
    String address;
    @Enumerated(EnumType.STRING)
    UserGender gender;
    LocalDate dob;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Education> educations;
}