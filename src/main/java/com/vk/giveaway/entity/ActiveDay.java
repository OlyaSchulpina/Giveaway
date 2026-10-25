package com.vk.giveaway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(name = "active_days",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "day"}))
public class ActiveDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private LocalDate day;

    protected ActiveDay() {
    }

    public ActiveDay(User user, LocalDate day) {
        this.user = user;
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDay() {
        return day;
    }
}
