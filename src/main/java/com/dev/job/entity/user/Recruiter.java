package com.dev.job.entity.user;

import jakarta.persistence.Column;

public class Recruiter {
    @Column(name = "company_name")
    String name;

    String description;

    @Column(name = "contact_email")
    String contactEmail;

    @Column(name = "contact_phone")
    String contactPhone;
}
