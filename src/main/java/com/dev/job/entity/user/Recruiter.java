package com.dev.job.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "recruiter")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Recruiter extends User {
    String companyName;
    @Lob
    @Column(columnDefinition = "TEXT")
    String description;
    String website;
    String phone;
    String address;
    boolean verified;
}
