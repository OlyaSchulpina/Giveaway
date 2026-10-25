package com.vk.giveaway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "winners")
public class Winner {

    public enum Type { DAILY, FINAL, RESERVE }

    public enum Status { PENDING, NOTIFIED, CONTACTS_RECEIVED, EXPIRED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Partner partner;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime drawnAt;
    private String fullName;
    private Boolean consent;

    protected Winner() {

    }

    public Winner(User user, Partner partner, Type type) {
        this.user = user;
        this.partner = partner;
        this.type = type;
        this.status = Status.PENDING;
        this.drawnAt = LocalDateTime.now();
    }

    public void markNotified() {
        this.status = Status.NOTIFIED;
    }

    public void expire() {
        this.status = Status.EXPIRED;
    }

    public void promoteToFinal() {
        this.type = Type.FINAL;
        this.status = Status.NOTIFIED;
    }

    public void saveContacts(String fullName) {
        this.fullName = fullName;
        this.consent = true;
        this.status = Status.CONTACTS_RECEIVED;
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

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getDrawnAt() {
        return drawnAt;
    }

    public String getFullName() {
        return fullName;
    }

    public Boolean getConsent() {
        return consent;
    }
}
