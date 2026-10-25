package com.vk.giveaway.service;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.entity.BotEvent;
import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.repository.BotEventRepository;
import com.vk.giveaway.repository.DailyEntryRepository;
import com.vk.giveaway.repository.PartnerRepository;
import com.vk.giveaway.repository.PromoRepository;
import com.vk.giveaway.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class StatsService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final PromoRepository promoRepository;
    private final DailyEntryRepository dailyEntryRepository;
    private final BotEventRepository botEventRepository;

    public StatsService(UserRepository userRepository,
                        PartnerRepository partnerRepository,
                        PromoRepository promoRepository,
                        DailyEntryRepository dailyEntryRepository,
                        BotEventRepository botEventRepository) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
        this.promoRepository = promoRepository;
        this.dailyEntryRepository = dailyEntryRepository;
        this.botEventRepository = botEventRepository;
    }

    public Dto.StatsResponse getStats() {
        List<User> users = userRepository.findAll();

        // новые пользователи по дням
        Map<LocalDate, Long> newByDay = new TreeMap<>();
        for (User user : users) {
            newByDay.merge(user.getCreatedAt().toLocalDate(), 1L, Long::sum);
        }

        // остаток промокодов и число участников по каждому партнёру
        Map<String, Long> promosLeft = new LinkedHashMap<>();
        Map<String, Long> participants = new LinkedHashMap<>();
        for (Partner partner : partnerRepository.findAll()) {
            promosLeft.put(partner.getName(), promoRepository.countByPartnerAndUserIsNull(partner));
            participants.put(partner.getName(), dailyEntryRepository.countByPartner(partner));
        }

        return new Dto.StatsResponse(
                users.size(),
                newByDay,
                botEventRepository.countByType(BotEvent.Type.PROMO_REQUEST),
                promoRepository.countByUserIsNotNull(),
                promosLeft,
                participants);
    }
}
