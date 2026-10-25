package com.vk.giveaway.controller;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.service.ChanceService;
import com.vk.giveaway.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChanceController {

    private final ChanceService chanceService;
    private final UserService userService;

    public ChanceController(ChanceService chanceService, UserService userService) {
        this.chanceService = chanceService;
        this.userService = userService;
    }
    // Получить шансы пользователя
    @GetMapping("/chances")
    public Dto.ChancesResponse chances(@RequestHeader("X-User-Id") Long userId) {
        return chanceService.info(userService.getUser(userId));
    }
}
