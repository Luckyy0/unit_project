package com.example.Event_Listener.listener;

import com.example.Event_Listener.dto.log.MethodEndEvent;
import com.example.Event_Listener.dto.log.MethodProcessingEvent;
import com.example.Event_Listener.dto.log.MethodStartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class MethodLogListener {
    @EventListener
    public void onMethodStart(MethodStartEvent event) {
        log.info("[START] %s - Args: %s".formatted(event.getMethodName(), Arrays.toString(event.getArgs())));
    }

    @EventListener
    public void onMethodProcessing(MethodProcessingEvent event) {
        log.info("[PROCESSING] %s - Time elapsed: %dms".formatted(event.getMethodName(), event.getExecutionTime()));
    }

    @EventListener
    public void onMethodEnd(MethodEndEvent event) {
        if (event.getException() != null) {
            log.info("[ERROR] %s - Exception: %s".formatted(event.getMethodName(), event.getException().getMessage()));
        } else {
            log.info("[END] %s - Result: %s".formatted(event.getMethodName(), event.getResult()));
        }
    }
}
