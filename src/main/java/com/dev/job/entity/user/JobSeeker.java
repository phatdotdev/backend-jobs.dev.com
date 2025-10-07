package com.dev.job.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "job_seeker")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobSeeker extends User{
    String firstname;
    String lastname;
    String phone;
    String address;
    @Enumerated(EnumType.STRING)
    UserGender gender;
    LocalDate dob;
}