package com.vk.giveaway.repository;

import com.vk.giveaway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
