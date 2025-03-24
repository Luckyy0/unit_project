package com.example.Event_Listener.listener;

import com.example.Event_Listener.dto.UserEventObject;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    // Xử lý đồng bộ, thứ tự ưu tiên cao nhất
    @EventListener
    @Order(1)
    public void handleLoggingUser(UserEventObject userEventObject) {
        System.out.println("Logging user registration: " + userEventObject.getCustomerName());
    }

    // Xử lý đồng bộ, thứ tự ưu tiên thấp hơn
    @Async
    @EventListener
    public void sendEmail(UserEventObject event) {
        System.out.println("Sending email to: " + event.getCustomerName());
    }
}
