package com.vk.giveaway.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Dto {

    public record StartResponse(Long userId, String text, List<String> buttons) {
    }

    public record PartnerResponse(Long id, String name, String channelUrl,
                                  String prizeName, LocalDate promoExpiresAt) {
    }

    public record PromoResponse(String status, String code, String text) {
    }

    public record GiveawayInfo(String text, String channelUrl) {
    }

    public record JoinResponse(String status, String text, Integer chances) {
    }

    public record ChancesResponse(int chances, int maxChances, List<LocalDate> days,
                                  List<String> partners, long daysLeft,
                                  String mainPrize, String text) {
    }

    public record WinnerResponse(Long id, Long userId, String type,
                                 String partnerName, String status) {
    }

    public record ContactsRequest(Long winnerId, String fullName, boolean consent) {
    }

    public record BroadcastRequest(String text) {
    }

    public record NotificationResponse(String text, LocalDateTime createdAt) {
    }

    public record StatsResponse(long totalUsers, Map<LocalDate, Long> newUsersByDay,
                                long promoRequests, long promosIssued,
                                Map<String, Long> promosLeftByPartner,
                                Map<String, Long> participantsByPartner) {
    }
}
