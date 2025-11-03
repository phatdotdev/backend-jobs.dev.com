package com.dev.job.entity.resource;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "image")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String fileName;
    String filePath;
    String fileType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime uploadedAt;

}
