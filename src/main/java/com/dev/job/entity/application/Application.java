package com.dev.job.entity.application;

import com.dev.job.entity.communication.Notification;
import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.resource.Document;
import com.dev.job.entity.resume.Resume;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "application",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"resume_id", "post_id"})
        }
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
    @JoinColumn(
            name = "post_id",
            foreignKey = @ForeignKey(name = "fk_application_post")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    JobPosting jobPosting;

    @ManyToOne
    @JoinColumn(
            name = "resume_id",
            foreignKey = @ForeignKey(name = "fk_application_resume")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    Resume resume;

    @Enumerated(EnumType.STRING)
    ApplicationState state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "application_id")
    List<Document> documents = new ArrayList<>();

    @Column(name = "applied_at")
    LocalDateTime appliedAt;
    @Column(name="updated_at")
    LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Notification> notifications = new ArrayList<>();

}
