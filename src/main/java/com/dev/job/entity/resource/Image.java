package com.dev.job.entity.resource;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Image {
    @Id
    UUID id;
}
