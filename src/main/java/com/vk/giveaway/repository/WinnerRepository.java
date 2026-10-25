package com.vk.giveaway.repository;

import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.entity.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WinnerRepository extends JpaRepository<Winner, Long> {

    List<Winner> findByUser(User user);

    List<Winner> findByPartnerAndType(Partner partner, Winner.Type type);

    List<Winner> findByTypeAndStatus(Winner.Type type, Winner.Status status);

    Optional<Winner> findFirstByTypeAndStatus(Winner.Type type, Winner.Status status);

    boolean existsByType(Winner.Type type);
}
