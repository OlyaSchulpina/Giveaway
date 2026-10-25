package com.vk.giveaway.repository;

import com.vk.giveaway.entity.BotEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotEventRepository extends JpaRepository<BotEvent, Long> {

    long countByType(BotEvent.Type type);
}
