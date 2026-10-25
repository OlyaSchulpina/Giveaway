package com.vk.giveaway.repository;

import com.vk.giveaway.entity.Notification;
import com.vk.giveaway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
