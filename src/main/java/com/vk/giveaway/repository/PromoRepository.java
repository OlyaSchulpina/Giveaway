package com.vk.giveaway.repository;

import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.Promo;
import com.vk.giveaway.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PromoRepository extends JpaRepository<Promo, Long> {

    Optional<Promo> findByUserAndPartner(User user, Partner partner);

    List<Promo> findByUser(User user);

    long countByUserIsNotNull();

    long countByPartnerAndUserIsNull(Partner partner);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Promo p where p.partner = :partner and p.user is null order by p.id")
    List<Promo> findFree(@Param("partner") Partner partner, Pageable pageable);
}
