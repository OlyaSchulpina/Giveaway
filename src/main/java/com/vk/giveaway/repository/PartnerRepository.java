package com.vk.giveaway.repository;

import com.vk.giveaway.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findFirstByGiveawayDate(LocalDate date);
}
