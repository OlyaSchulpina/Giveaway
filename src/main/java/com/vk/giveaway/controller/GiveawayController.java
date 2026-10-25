package com.vk.giveaway.controller;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.service.GiveawayService;
import com.vk.giveaway.service.PartnerService;
import com.vk.giveaway.service.SubscriptionService;
import com.vk.giveaway.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GiveawayController {

    private final GiveawayService giveawayService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final PartnerService partnerService;

    public GiveawayController(GiveawayService giveawayService,
                              SubscriptionService subscriptionService,
                              UserService userService,
                              PartnerService partnerService) {
        this.giveawayService = giveawayService;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.partnerService = partnerService;
    }
    // Участие в розыгрыше
    @GetMapping("/giveaway")
    public Dto.GiveawayInfo info() {
        return giveawayService.info();
    }
    // Проверка подписки пользователя
    @PostMapping("/giveaway/check")
    public Dto.JoinResponse check(@RequestHeader("X-User-Id") Long userId) {
        return giveawayService.check(userId);
    }

    // Имитация подписки
    @PostMapping("/dev/subscribe")
    public Map<String, String> subscribe(@RequestHeader("X-User-Id") Long userId) {
        subscriptionService.subscribe(userService.getUser(userId), partnerService.todayPartner());
        return Map.of("result", "Подписка имитирована");
    }
}
