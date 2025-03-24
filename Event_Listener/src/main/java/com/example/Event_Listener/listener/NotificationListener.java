package com.example.Event_Listener.listener;

import com.example.Event_Listener.dto.order.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class NotificationListener {
    // Chỉ xử lý khi amount > 100
    @EventListener(condition = "#event.amount.compareTo(0) > 0")
    @Order(4)
    public void sendPremiumNotification(OrderCreatedEvent event) {
        log.info("VIP Customer, Order {} created!", event.getOrderId());
    }

    // Xử lý sau khi transaction commit thành công
    @Order(3)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(OrderCreatedEvent event) {
        log.info("Order committed to DB: {}", event.getOrderId());
    }
}
