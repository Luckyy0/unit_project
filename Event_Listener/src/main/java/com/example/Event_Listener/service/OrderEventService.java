package com.example.Event_Listener.service;

import com.example.Event_Listener.dto.order.OrderCancelEvent;
import com.example.Event_Listener.dto.order.OrderCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventService {
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createOrder(OrderCreatedEvent orderCreatedEvent) {
        eventPublisher.publishEvent(orderCreatedEvent);
    }

    @Transactional
    public void cancelOrder(OrderCancelEvent orderCancelEvent) {
        eventPublisher.publishEvent(orderCancelEvent);
    }
}
