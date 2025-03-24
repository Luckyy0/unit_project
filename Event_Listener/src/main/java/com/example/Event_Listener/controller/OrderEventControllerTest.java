package com.example.Event_Listener.controller;

import com.example.Event_Listener.dto.UserEventObject;
import com.example.Event_Listener.dto.order.OrderCancelEvent;
import com.example.Event_Listener.dto.order.OrderCreatedEvent;
import com.example.Event_Listener.service.OrderEventService;
import com.example.Event_Listener.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderEventControllerTest {
    private final OrderEventService orderEventService;

    @PostMapping("/created_order")
    public void createdOrder() {
        orderEventService.createOrder(OrderCreatedEvent.builder().orderId("1L").amount(100L).build());
    }

    @PostMapping("/cancel_order")
    public void cancelOrder() {
        orderEventService.cancelOrder(OrderCancelEvent.builder().orderId("1L").reason("slow").build());
    }
}
