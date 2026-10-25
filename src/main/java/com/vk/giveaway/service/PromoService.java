package com.vk.giveaway.service;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.entity.BotEvent;
import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.Promo;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.repository.BotEventRepository;
import com.vk.giveaway.repository.PromoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PromoService {

    private final PromoRepository promoRepository;
    private final BotEventRepository botEventRepository;
    private final UserService userService;
    private final PartnerService partnerService;
    private final ChanceService chanceService;

    public PromoService(PromoRepository promoRepository,
                        BotEventRepository botEventRepository,
                        UserService userService,
                        PartnerService partnerService,
                        ChanceService chanceService) {
        this.promoRepository = promoRepository;
        this.botEventRepository = botEventRepository;
        this.userService = userService;
        this.partnerService = partnerService;
        this.chanceService = chanceService;
    }

    @Transactional
    public Dto.PromoResponse getPromo(Long userId) {
        User user = userService.getUser(userId);
        Partner partner = partnerService.todayPartner();


        botEventRepository.save(new BotEvent(user, BotEvent.Type.PROMO_REQUEST));


        Optional<Promo> old = promoRepository.findByUserAndPartner(user, partner);
        if (old.isPresent()) {
            return issuedResponse(old.get(), partner);
        }


        List<Promo> free = promoRepository.findFree(partner, PageRequest.of(0, 1));
        if (free.isEmpty()) {
            String text = "Промокоды от " + partner.getName()
                    + " уже закончились — их разобрали быстрее всех. "
                    + "Но ты всё ещё можешь подписаться на партнёра и принять участие в розыгрыше призов дня.";
            return new Dto.PromoResponse("SOLD_OUT", null, text);
        }

        Promo promo = free.get(0);
        promo.assignTo(user);

        chanceService.markActive(user);
        return issuedResponse(promo, partner);
    }

    private Dto.PromoResponse issuedResponse(Promo promo, Partner partner) {
        String text = "Твой фестивальный бонус от " + partner.getName() + " готов. "
                + "Промокод: " + promo.getCode() + ". "
                + "Используй его до " + partner.getPromoExpiresAt() + ". "
                + "Теперь можешь принять участие в розыгрыше призов дня.";
        return new Dto.PromoResponse("ISSUED", promo.getCode(), text);
    }
}
