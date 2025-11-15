package com.dev.job.entity.user;

import com.dev.job.entity.specification.Expertise;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "expert")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Expert extends User {
    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Expertise> expertises;

    String phone;
}
