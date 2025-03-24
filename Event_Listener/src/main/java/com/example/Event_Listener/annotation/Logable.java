package com.example.Event_Listener.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Logable {
    String value() default ""; // Mô tả tùy chọn
    boolean logParams() default true; // Có log tham số không?
    boolean logResult() default true; // Có log kết quả không?
}
