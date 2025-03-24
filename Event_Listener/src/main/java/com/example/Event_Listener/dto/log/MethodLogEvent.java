package com.example.Event_Listener.dto.log;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class MethodLogEvent {
    private String methodName;
    private Object[] args;
    @Builder.Default
    private long timestamp = System.currentTimeMillis();
}
