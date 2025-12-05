package com.dev.job.entity.specification;

import com.dev.job.entity.user.Expert;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "expertise")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Expertise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String title;
    String field;

    @Lob
    String description;
    int yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    Expert expert;
}
