package com.example.Event_Listener.dto.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MethodEndEvent extends MethodLogEvent {
    private Object result;
    private Throwable exception;
}
