package com.example.Event_Listener.service;

import com.example.Event_Listener.dto.UserEventObject;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventService {
    private final ApplicationEventPublisher eventPublisher;

    public void registerCustomer(UserEventObject userEventObject) {
        eventPublisher.publishEvent(userEventObject);
    }
}
