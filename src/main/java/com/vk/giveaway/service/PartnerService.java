package com.vk.giveaway.service;

import com.vk.giveaway.entity.Partner;
import com.vk.giveaway.exception.ApiException;
import com.vk.giveaway.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final String fakeDate;

    public PartnerService(PartnerRepository partnerRepository,
                          @Value("${app.fake-date:}") String fakeDate) {
        this.partnerRepository = partnerRepository;
        this.fakeDate = fakeDate;
    }

    public LocalDate today() {
        return fakeDate.isBlank() ? LocalDate.now() : LocalDate.parse(fakeDate);
    }

    public Partner todayPartner() {
        return partnerRepository.findFirstByGiveawayDate(today())
                .orElseThrow(() -> new ApiException("На сегодня партнёра нет"));
    }
}
