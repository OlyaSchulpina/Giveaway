package com.vk.giveaway.controller;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.service.DrawService;
import com.vk.giveaway.service.NotificationService;
import com.vk.giveaway.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PrizeController {

    private final NotificationService notificationService;
    private final DrawService drawService;
    private final UserService userService;

    public PrizeController(NotificationService notificationService,
                           DrawService drawService,
                           UserService userService) {
        this.notificationService = notificationService;
        this.drawService = drawService;
        this.userService = userService;
    }
    // Получение всех уведомлений пользователя
    @GetMapping("/notifications")
    public List<Dto.NotificationResponse> notifications(@RequestHeader("X-User-Id") Long userId) {
        return notificationService.listFor(userService.getUser(userId));
    }
    // Получение всех призов пользователя
    @GetMapping("/my-winnings")
    public List<Dto.WinnerResponse> myWinnings(@RequestHeader("X-User-Id") Long userId) {
        return drawService.myWinnings(userId);
    }
    // Добавление контактных данных пользователя
    @PostMapping("/winner/contacts")
    public Map<String, String> contacts(@RequestHeader("X-User-Id") Long userId,
                                        @RequestBody Dto.ContactsRequest request) {
        drawService.saveContacts(userId, request);
        return Map.of("result", "Данные сохранены");
    }
}
