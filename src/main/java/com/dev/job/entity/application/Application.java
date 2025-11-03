package com.dev.job.entity.application;

import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.resume.Resume;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "application",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "resume_id"})
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_application_post", foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES job_posting(id) ON DELETE CASCADE"))
    JobPosting jobPosting;

    @ManyToOne
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "fk_application_resume", foreignKeyDefinition = "FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE"))
    Resume resume;

    @Enumerated(EnumType.STRING)
    ApplicationState state;

    @Column(name = "applied_at")
    LocalDateTime appliedAt;
    @Column(name="updated_at")
    LocalDateTime updatedAt;
}
