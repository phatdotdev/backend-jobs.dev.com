package com.dev.job.entity.communication;

import com.dev.job.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Notification {

    Long id;
    String title;
    String message;
    String type;
    LocalDate createAt;
    String link;


    List<User> receivers;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender;
}
