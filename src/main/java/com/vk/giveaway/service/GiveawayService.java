package com.vk.giveaway.service;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.entity.DailyEntry;
import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.repository.DailyEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiveawayService {

    private final DailyEntryRepository dailyEntryRepository;
    private final UserService userService;
    private final PartnerService partnerService;
    private final ChanceService chanceService;
    private final SubscriptionService subscriptionService;

    public GiveawayService(DailyEntryRepository dailyEntryRepository,
                           UserService userService,
                           PartnerService partnerService,
                           ChanceService chanceService,
                           SubscriptionService subscriptionService) {
        this.dailyEntryRepository = dailyEntryRepository;
        this.userService = userService;
        this.partnerService = partnerService;
        this.chanceService = chanceService;
        this.subscriptionService = subscriptionService;
    }

    public Dto.GiveawayInfo info() {
        Partner partner = partnerService.todayPartner();
        String text = "Чтобы попасть в розыгрыш призов от " + partner.getName()
                + ", подпишись на аккаунт партнёра.";
        return new Dto.GiveawayInfo(text, partner.getChannelUrl());
    }

    @Transactional
    public Dto.JoinResponse check(Long userId) {
        User user = userService.getUser(userId);
        Partner partner = partnerService.todayPartner();

        if (dailyEntryRepository.existsByUserAndPartner(user, partner)) {
            String text = "Ты уже участвуешь в сегодняшнем розыгрыше ✅\n"
                    + "Результаты сообщим после подведения итогов.";
            return new Dto.JoinResponse("ALREADY_JOINED", text, chanceService.count(user));
        }

        if (!subscriptionService.isSubscribed(user, partner)) {
            String text = "Пока не вижу подписку 👀\n\n"
                    + "Подпишись на аккаунт " + partner.getName() + ", вернись и попробуй ещё раз.";
            return new Dto.JoinResponse("NOT_SUBSCRIBED", text, chanceService.count(user));
        }

        dailyEntryRepository.save(new DailyEntry(user, partner));
        chanceService.markActive(user);

        int chances = chanceService.count(user);
        String text = "Ты в лайнапе. "
                + "Заявка на розыгрыш призов от " + partner.getName() + " зарегистрирована."
                + "Возвращайся завтра — на сцену выйдет новый партнёр."
                + "Накоплено шансов: " + chances + " из " + ChanceService.MAX_CHANCES;
        return new Dto.JoinResponse("JOINED", text, chances);
    }
}
