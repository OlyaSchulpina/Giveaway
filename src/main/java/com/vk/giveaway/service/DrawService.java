package com.vk.giveaway.service;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.entity.ActiveDay;
import com.vk.giveaway.entity.DailyEntry;
import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.entity.Winner;
import com.vk.giveaway.exception.ApiException;
import com.vk.giveaway.repository.ActiveDayRepository;
import com.vk.giveaway.repository.DailyEntryRepository;
import com.vk.giveaway.repository.PartnerRepository;
import com.vk.giveaway.repository.WinnerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class DrawService {

    private final PartnerRepository partnerRepository;
    private final DailyEntryRepository dailyEntryRepository;
    private final ActiveDayRepository activeDayRepository;
    private final WinnerRepository winnerRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final String finalPrize;

    public DrawService(PartnerRepository partnerRepository,
                       DailyEntryRepository dailyEntryRepository,
                       ActiveDayRepository activeDayRepository,
                       WinnerRepository winnerRepository,
                       UserService userService,
                       NotificationService notificationService,
                       @Value("${app.final-prize}") String finalPrize) {
        this.partnerRepository = partnerRepository;
        this.dailyEntryRepository = dailyEntryRepository;
        this.activeDayRepository = activeDayRepository;
        this.winnerRepository = winnerRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.finalPrize = finalPrize;
    }


    @Transactional
    public List<Dto.WinnerResponse> drawDaily(Long partnerId) {
        Partner partner = getPartner(partnerId);

        if (!winnerRepository.findByPartnerAndType(partner, Winner.Type.DAILY).isEmpty()) {
            throw new ApiException("Розыгрыш этого партнёра уже проведён");
        }

        List<DailyEntry> entries = dailyEntryRepository.findByPartner(partner);
        if (entries.isEmpty()) {
            throw new ApiException("Нет участников");
        }

        Collections.shuffle(entries);
        int count = Math.min(partner.getWinnersCount(), entries.size());

        List<Dto.WinnerResponse> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            Winner winner = new Winner(entries.get(i).getUser(), partner, Winner.Type.DAILY);
            winnerRepository.save(winner);
            result.add(toResponse(winner));
        }
        return result;
    }

    @Transactional
    public int confirmDaily(Long partnerId) {
        Partner partner = getPartner(partnerId);
        int sent = 0;
        for (Winner winner : winnerRepository.findByPartnerAndType(partner, Winner.Type.DAILY)) {
            if (winner.getStatus() != Winner.Status.PENDING) {
                continue;
            }
            winner.markNotified();
            String text = "Ты в числе хедлайнеров дня. "
                    + "Ты выиграл " + partner.getPrizeName() + " от " + partner.getName() + "."
                    + "Чтобы получить приз, оставь контактные данные в разделе «Мои призы».";
            notificationService.send(winner.getUser(), text);
            sent++;
        }
        return sent;
    }

    @Transactional
    public List<Dto.WinnerResponse> drawFinal(int winnersCount, int reserveCount) {
        if (winnerRepository.existsByType(Winner.Type.FINAL)) {
            throw new ApiException("Финал уже проведён");
        }
        List<User> tickets = new ArrayList<>();
        for (ActiveDay day : activeDayRepository.findAll()) {
            tickets.add(day.getUser());
        }
        if (tickets.isEmpty()) {
            throw new ApiException("Нет билетов");
        }

        Collections.shuffle(tickets);

        List<User> order = new ArrayList<>(new LinkedHashSet<>(tickets));

        List<Dto.WinnerResponse> result = new ArrayList<>();
        for (int i = 0; i < order.size() && i < winnersCount + reserveCount; i++) {
            Winner.Type type = i < winnersCount ? Winner.Type.FINAL : Winner.Type.RESERVE;
            Winner winner = new Winner(order.get(i), null, type);
            winnerRepository.save(winner);
            result.add(toResponse(winner));
        }
        return result;
    }

    @Transactional
    public int confirmFinal() {
        int sent = 0;
        for (Winner winner : winnerRepository.findByTypeAndStatus(Winner.Type.FINAL, Winner.Status.PENDING)) {
            winner.markNotified();
            notifyFinalWinner(winner.getUser());
            sent++;
        }
        return sent;
    }
    @Transactional
    public Dto.WinnerResponse replaceWinner(Long winnerId) {
        Winner old = winnerRepository.findById(winnerId)
                .orElseThrow(() -> new ApiException("Победитель не найден"));
        if (old.getType() != Winner.Type.FINAL) {
            throw new ApiException("Заменять можно только победителей финала");
        }
        Winner reserve = winnerRepository
                .findFirstByTypeAndStatus(Winner.Type.RESERVE, Winner.Status.PENDING)
                .orElseThrow(() -> new ApiException("Резервных победителей не осталось"));

        old.expire();
        reserve.promoteToFinal();
        notifyFinalWinner(reserve.getUser());
        return toResponse(reserve);
    }

    private void notifyFinalWinner(User user) {
        String text = "Твой сентябрь выходит на главную сцену 🔥\n"
                + "Ты выиграл " + finalPrize + "!\n\n"
                + "Чтобы подтвердить получение приза, оставь контактные данные в разделе «Мои призы».";
        notificationService.send(user, text);
    }

    @Transactional
    public void saveContacts(Long userId, Dto.ContactsRequest request) {
        User user = userService.getUser(userId);
        Winner winner = winnerRepository.findById(request.winnerId())
                .orElseThrow(() -> new ApiException("Победитель не найден"));

        if (!winner.getUser().getId().equals(user.getId())) {
            throw new ApiException("Это не твой приз");
        }
        if (winner.getStatus() != Winner.Status.NOTIFIED) {
            throw new ApiException("Сейчас нельзя оставить данные");
        }
        if (!request.consent()) {
            throw new ApiException("Нужно согласие на передачу данных");
        }
        if (request.fullName() == null || request.fullName().isBlank()) {
            throw new ApiException("Укажи имя и фамилию");
        }
        winner.saveContacts(request.fullName());
    }

    public List<Dto.WinnerResponse> myWinnings(Long userId) {
        User user = userService.getUser(userId);
        List<Dto.WinnerResponse> result = new ArrayList<>();
        for (Winner winner : winnerRepository.findByUser(user)) {
            if (winner.getStatus() == Winner.Status.NOTIFIED
                    || winner.getStatus() == Winner.Status.CONTACTS_RECEIVED) {
                result.add(toResponse(winner));
            }
        }
        return result;
    }

    public String exportDaily(Long partnerId) {
        Partner partner = getPartner(partnerId);

        Set<Long> winnerIds = new HashSet<>();
        for (Winner winner : winnerRepository.findByPartnerAndType(partner, Winner.Type.DAILY)) {
            winnerIds.add(winner.getUser().getId());
        }

        StringBuilder csv = new StringBuilder("userId;name;registeredAt;isWinner\n");
        for (DailyEntry entry : dailyEntryRepository.findByPartner(partner)) {
            csv.append(entry.getUser().getId()).append(';')
               .append(entry.getUser().getName()).append(';')
               .append(entry.getRegisteredAt()).append(';')
               .append(winnerIds.contains(entry.getUser().getId())).append('\n');
        }
        return csv.toString();
    }


    private Partner getPartner(Long id) {
        return partnerRepository.findById(id)
                .orElseThrow(() -> new ApiException("Партнёр не найден"));
    }

    private Dto.WinnerResponse toResponse(Winner winner) {
        String partnerName = winner.getPartner() == null ? null : winner.getPartner().getName();
        return new Dto.WinnerResponse(winner.getId(), winner.getUser().getId(),
                winner.getType().name(), partnerName, winner.getStatus().name());
    }
}
