package com.dev.job.entity.resource;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "links")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Link {
    @Id
    @GeneratedValue
    Long id;
    String label;
    String url;
    OwnerType ownerType;
    Long ownerId;
    @Column(name = "create_at")
    LocalDate createAt;
    @Column(name = "update_at")
    LocalDate updateAt;
}
