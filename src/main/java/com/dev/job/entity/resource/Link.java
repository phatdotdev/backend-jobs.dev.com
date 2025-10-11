package com.dev.job.entity.resource;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "link")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Link {
    @Id
    UUID id;
    String label;
    String link;
}
