package com.dev.job.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "recruiter")
public class Recruiter extends User{
    String name;
    String description;
    String phone;
    String address;
    boolean verified;
}
