package com.vk.giveaway.controller;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.service.PromoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromoController {

    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    @PostMapping("/promo")
    public Dto.PromoResponse getPromo(@RequestHeader("X-User-Id") Long userId) {
        return promoService.getPromo(userId);
    }
}
