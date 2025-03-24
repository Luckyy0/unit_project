package com.example.Event_Listener.listener;

import com.example.Event_Listener.dto.order.OrderCancelEvent;
import com.example.Event_Listener.dto.order.OrderCreatedEvent;
import com.example.Event_Listener.dto.order.OrderEvent;
import com.example.Event_Listener.exception.PaymentFailedException;
import com.example.Event_Listener.gateway.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * Giả sử có 4 event A async order 1, B async order 2, C order 3, D order 4
 * A run đầu tiên -> B run (không đợi A hoàn thành) -> C run (không đợi B hoàn thành) -> D run (sau khi C hoàn thành)
 */
@Component
@Slf4j
public class OrderEventListener {

    @EventListener
    @Order(1)
    public void handleAllOrderEvents(OrderEvent event) {
        log.info("Generic handling for order event: {}", event.getClass().getSimpleName());
    }

    @Async
    @EventListener
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1))
    @Order(2)
    public void handlePayment(OrderCreatedEvent event) {
        // Giả lập call API thanh toán
        if (PaymentGateway.processPayment()) {
            log.info("Payment processed for order: {}", event.getOrderId());
        } else {
            log.info("retry");
            throw new PaymentFailedException("Payment failed!");
        }
    }

    //  Fallback khi retry thất bại hoàn toàn
    @Recover
    public void recoverPayment(PaymentFailedException e, OrderCreatedEvent event) {
        log.info("Mark order {} as failed!", event.getOrderId());
    }

    @EventListener
    @Order(2)
    public void logOrderCancellation(OrderCancelEvent event) {
        log.info("ORDER_CANCELLED: {}. Reason: {}", event.getOrderId(), event.getReason());
    }
}
