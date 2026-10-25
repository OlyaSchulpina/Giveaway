package com.vk.giveaway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "promos")
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne
    private Partner partner;

    @ManyToOne
    private User user;

    private LocalDateTime issuedAt;

    protected Promo() {

    }

    public Promo(String code, Partner partner) {
        this.code = code;
        this.partner = partner;
    }

    public void assignTo(User user) {
        this.user = user;
        this.issuedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Partner getPartner() {
        return partner;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
}
