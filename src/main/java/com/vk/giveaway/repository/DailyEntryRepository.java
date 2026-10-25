package com.vk.giveaway.repository;

import com.vk.giveaway.entity.DailyEntry;
import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyEntryRepository extends JpaRepository<DailyEntry, Long> {

    boolean existsByUserAndPartner(User user, Partner partner);

    List<DailyEntry> findByUser(User user);

    List<DailyEntry> findByPartner(Partner partner);

    long countByPartner(Partner partner);
}
