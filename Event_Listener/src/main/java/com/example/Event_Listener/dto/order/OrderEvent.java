package com.example.Event_Listener.dto.order;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class OrderEvent {
    private String orderId;
}
