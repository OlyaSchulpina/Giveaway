package com.vk.giveaway.service;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.entity.ActiveDay;
import com.vk.giveaway.entity.DailyEntry;
import com.vk.giveaway.entity.Promo;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.repository.ActiveDayRepository;
import com.vk.giveaway.repository.DailyEntryRepository;
import com.vk.giveaway.repository.PromoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChanceService {

    public static final int MAX_CHANCES = 10;

    private final ActiveDayRepository activeDayRepository;
    private final PromoRepository promoRepository;
    private final DailyEntryRepository dailyEntryRepository;
    private final PartnerService partnerService;
    private final String endDate;
    private final String finalPrize;

    public ChanceService(ActiveDayRepository activeDayRepository,
                         PromoRepository promoRepository,
                         DailyEntryRepository dailyEntryRepository,
                         PartnerService partnerService,
                         @Value("${app.end-date}") String endDate,
                         @Value("${app.final-prize}") String finalPrize) {
        this.activeDayRepository = activeDayRepository;
        this.promoRepository = promoRepository;
        this.dailyEntryRepository = dailyEntryRepository;
        this.partnerService = partnerService;
        this.endDate = endDate;
        this.finalPrize = finalPrize;
    }

    public void markActive(User user) {
        LocalDate today = partnerService.today();
        if (activeDayRepository.existsByUserAndDay(user, today)) {
            return;
        }
        if (activeDayRepository.countByUser(user) >= MAX_CHANCES) {
            return;
        }
        activeDayRepository.save(new ActiveDay(user, today));
    }

    public int count(User user) {
        return (int) activeDayRepository.countByUser(user);
    }

    public Dto.ChancesResponse info(User user) {
        List<LocalDate> days = new ArrayList<>();
        for (ActiveDay d : activeDayRepository.findByUserOrderByDay(user)) {
            days.add(d.getDay());
        }

        Set<String> partners = new LinkedHashSet<>();
        for (Promo p : promoRepository.findByUser(user)) {
            partners.add(p.getPartner().getName());
        }
        for (DailyEntry e : dailyEntryRepository.findByUser(user)) {
            partners.add(e.getPartner().getName());
        }

        long daysLeft = Math.max(0,
                ChronoUnit.DAYS.between(partnerService.today(), LocalDate.parse(endDate)));

        String text = "Твой фестивальный пропуск\n"
                + "Накоплено шансов: " + days.size() + " из " + MAX_CHANCES + "\n"
                + "Чем больше дней розыгрыша ты посетишь, тем выше вероятность выиграть главный приз.";

        return new Dto.ChancesResponse(days.size(), MAX_CHANCES, days,
                new ArrayList<>(partners), daysLeft, finalPrize, text);
    }
}
