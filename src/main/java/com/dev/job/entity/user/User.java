package com.dev.job.entity.user;

import com.dev.job.entity.resource.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(length = 255)
    String address;

    @Column(length = 20)
    String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "picture_id")
    Image picture;

    @Column(name = "create_at")
    LocalDate createAt;

    @Column(name = "update_at")
    LocalDate updateAt;

    @Column(name = "update_by")
    Long updateBy;
}
