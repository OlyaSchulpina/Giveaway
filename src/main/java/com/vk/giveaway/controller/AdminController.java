package com.vk.giveaway.controller;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.exception.ApiException;
import com.vk.giveaway.service.DrawService;
import com.vk.giveaway.service.NotificationService;
import com.vk.giveaway.service.StatsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DrawService drawService;
    private final StatsService statsService;
    private final NotificationService notificationService;
    private final String adminKey;

    public AdminController(DrawService drawService,
                           StatsService statsService,
                           NotificationService notificationService,
                           @Value("${app.admin-key}") String adminKey) {
        this.drawService = drawService;
        this.statsService = statsService;
        this.notificationService = notificationService;
        this.adminKey = adminKey;
    }
    // Аутенфикация админа
    private void checkKey(String key) {
        if (!adminKey.equals(key)) {
            throw new ApiException("Неверный ключ администратора");
        }
    }
    // Провести розыгрыш дня
    @PostMapping("/draws/daily/{partnerId}")
    public List<Dto.WinnerResponse> drawDaily(@RequestHeader("X-Admin-Key") String key,
                                              @PathVariable Long partnerId) {
        checkKey(key);
        return drawService.drawDaily(partnerId);
    }
    // Подтвердить результаты и сделать рассылку
    @PostMapping("/draws/daily/{partnerId}/confirm")
    public Map<String, Object> confirmDaily(@RequestHeader("X-Admin-Key") String key,
                                            @PathVariable Long partnerId) {
        checkKey(key);
        return Map.of("notificationsSent", drawService.confirmDaily(partnerId));
    }
    // Экспортировать результаты в CSV
    @GetMapping("/draws/daily/{partnerId}/export")
    public ResponseEntity<String> export(@RequestHeader("X-Admin-Key") String key,
                                         @PathVariable Long partnerId) {
        checkKey(key);
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv; charset=UTF-8")
                .header("Content-Disposition", "attachment; filename=daily-" + partnerId + ".csv")
                .body(drawService.exportDaily(partnerId));
    }
    // Провести финальный розыгрыш
    @PostMapping("/draws/final")
    public List<Dto.WinnerResponse> drawFinal(@RequestHeader("X-Admin-Key") String key,
                                              @RequestParam(defaultValue = "5") int winners,
                                              @RequestParam(defaultValue = "3") int reserve) {
        checkKey(key);
        return drawService.drawFinal(winners, reserve);
    }
    // Подтвердить результаты и сделать рассылку
    @PostMapping("/draws/final/confirm")
    public Map<String, Object> confirmFinal(@RequestHeader("X-Admin-Key") String key) {
        checkKey(key);
        return Map.of("notificationsSent", drawService.confirmFinal());
    }
    // Заменить победителя на резервного
    @PostMapping("/winners/{winnerId}/replace")
    public Dto.WinnerResponse replace(@RequestHeader("X-Admin-Key") String key,
                                      @PathVariable Long winnerId) {
        checkKey(key);
        return drawService.replaceWinner(winnerId);
    }
    // Сделать рассылку
    @PostMapping("/broadcast")
    public Map<String, Object> broadcast(@RequestHeader("X-Admin-Key") String key,
                                         @RequestBody Dto.BroadcastRequest request) {
        checkKey(key);
        return Map.of("sentTo", notificationService.sendToAll(request.text()));
    }
    // Получить статистику работы бота
    @GetMapping("/stats")
    public Dto.StatsResponse stats(@RequestHeader("X-Admin-Key") String key) {
        checkKey(key);
        return statsService.getStats();
    }
}
