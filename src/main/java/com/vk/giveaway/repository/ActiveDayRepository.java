package com.vk.giveaway.repository;

import com.vk.giveaway.entity.ActiveDay;
import com.vk.giveaway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActiveDayRepository extends JpaRepository<ActiveDay, Long> {

    boolean existsByUserAndDay(User user, LocalDate day);

    long countByUser(User user);

    List<ActiveDay> findByUserOrderByDay(User user);
}
