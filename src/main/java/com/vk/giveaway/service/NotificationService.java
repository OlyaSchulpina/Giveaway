package com.vk.giveaway.service;

import com.vk.giveaway.dto.Dto;
import com.vk.giveaway.entity.Notification;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.repository.NotificationRepository;
import com.vk.giveaway.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void send(User user, String text) {
        notificationRepository.save(new Notification(user, text));
    }

    @Transactional
    public int sendToAll(String text) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            send(user, text);
        }
        return users.size();
    }

    public List<Dto.NotificationResponse> listFor(User user) {
        List<Dto.NotificationResponse> result = new ArrayList<>();
        for (Notification n : notificationRepository.findByUserOrderByCreatedAtDesc(user)) {
            result.add(new Dto.NotificationResponse(n.getText(), n.getCreatedAt()));
        }
        return result;
    }
}
