package com.example.Event_Listener.aspect;

import com.example.Event_Listener.annotation.Logable;
import com.example.Event_Listener.dto.log.MethodEndEvent;
import com.example.Event_Listener.dto.log.MethodProcessingEvent;
import com.example.Event_Listener.dto.log.MethodStartEvent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final ApplicationEventPublisher eventPublisher;

    @Around("@annotation(logable)")
    public Object logMethod(ProceedingJoinPoint joinPoint, Logable logable) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = logable.logParams() ? joinPoint.getArgs() : new Object[0];

        // Phát event trước khi gọi method
        eventPublisher.publishEvent(MethodStartEvent.builder().methodName(methodName).args(args).build());

        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable exception = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            exception = ex;
            throw ex;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;

            // Phát event trong khi xử lý (có thể trigger nhiều lần)
            eventPublisher.publishEvent(MethodProcessingEvent.builder().methodName(methodName).args(args).executionTime(executionTime).build());

            // Phát event sau khi kết thúc
            eventPublisher.publishEvent(
                    MethodEndEvent.builder()
                            .methodName(methodName)
                            .args(args).result(logable.logResult() ? result : null)
                            .exception(exception).build()
            );
        }
    }
}
