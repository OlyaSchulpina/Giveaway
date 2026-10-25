package com.vk.giveaway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;


@Entity
@Table(name = "daily_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "partner_id"}))
public class DailyEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Partner partner;

    private LocalDateTime registeredAt;

    protected DailyEntry() {
    }

    public DailyEntry(User user, Partner partner) {
        this.user = user;
        this.partner = partner;
        this.registeredAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Partner getPartner() {
        return partner;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}
