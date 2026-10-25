package com.vk.giveaway.service;

import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.entity.User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// Имитация проверки подписки. В настоящем боте нужно будет по-другому проверять подписку
@Service
public class SubscriptionService {

    private final Set<String> subscribed = ConcurrentHashMap.newKeySet();

    public void subscribe(User user, Partner partner) {
        subscribed.add(user.getId() + ":" + partner.getId());
    }

    public boolean isSubscribed(User user, Partner partner) {
        return subscribed.contains(user.getId() + ":" + partner.getId());
    }
}
