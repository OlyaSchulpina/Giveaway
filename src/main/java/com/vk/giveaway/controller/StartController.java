package com.vk.giveaway.controller;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.service.PartnerService;
import com.vk.giveaway.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StartController {

    private final UserService userService;
    private final PartnerService partnerService;

    public StartController(UserService userService, PartnerService partnerService) {
        this.userService = userService;
        this.partnerService = partnerService;
    }
    // "Кнопка" Начать
    @PostMapping("/start")
    public Dto.StartResponse start(@RequestParam(required = false) String name) {
        User user = userService.createUser(name);
        return new Dto.StartResponse(user.getId(),
                "Добро пожаловать в Бот розыгрышей",
                List.of("Получить промокод", "Участвовать в розыгрыше",
                        "Как участвовать", "Мои шансы", "Правила акции"));
    }
    // Получение партнера дня
    @GetMapping("/partner/today")
    public Dto.PartnerResponse today() {
        Partner partner = partnerService.todayPartner();
        return new Dto.PartnerResponse(partner.getId(), partner.getName(), partner.getChannelUrl(),
                partner.getPrizeName(), partner.getPromoExpiresAt());
    }
    // Запрос на получение инструкции
    @GetMapping("/info/how")
    public Map<String, String> how() {
        return Map.of("text", "1. Получи промокод партнёра."
                + "2. Подпишись на аккаунт партнёра и нажми «Проверить подписку»."
                + "3. Возвращайся каждый день: один активный день = один шанс на главный приз (максимум 10).");
    }
    // Запрос на получение правил акций
    @GetMapping("/info/rules")
    public Map<String, String> rules() {
        return Map.of("text", "ТУТ ТЕКСТ ПРАВИЛ АКЦИИ");
    }
}
