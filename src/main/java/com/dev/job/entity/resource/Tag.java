package com.dev.job.entity.resource;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Tag {
    @Id
    UUID id;
}
