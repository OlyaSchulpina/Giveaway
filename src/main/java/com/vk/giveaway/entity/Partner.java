package com.vk.giveaway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "partners")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String channelUrl;
    private LocalDate giveawayDate;
    private String prizeName;
    private int winnersCount;
    private LocalDate promoExpiresAt;

    protected Partner() {
    }

    public Partner(String name, String channelUrl, LocalDate giveawayDate,
                   String prizeName, int winnersCount, LocalDate promoExpiresAt) {
        this.name = name;
        this.channelUrl = channelUrl;
        this.giveawayDate = giveawayDate;
        this.prizeName = prizeName;
        this.winnersCount = winnersCount;
        this.promoExpiresAt = promoExpiresAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public LocalDate getGiveawayDate() {
        return giveawayDate;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public int getWinnersCount() {
        return winnersCount;
    }

    public LocalDate getPromoExpiresAt() {
        return promoExpiresAt;
    }
}
