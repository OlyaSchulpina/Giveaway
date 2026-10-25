package com.vk.giveaway.service;

import com.vk.giveaway.entity.BotEvent;
import com.vk.giveaway.entity.User;
import com.vk.giveaway.exception.ApiException;
import com.vk.giveaway.repository.BotEventRepository;
import com.vk.giveaway.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BotEventRepository botEventRepository;

    public UserService(UserRepository userRepository, BotEventRepository botEventRepository) {
        this.userRepository = userRepository;
        this.botEventRepository = botEventRepository;
    }

    public User createUser(String name) {
        User user = new User(name == null || name.isBlank() ? "Гость" : name);
        userRepository.save(user);
        botEventRepository.save(new BotEvent(user, BotEvent.Type.START));
        return user;
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException("Пользователь не найден."));
    }
}
