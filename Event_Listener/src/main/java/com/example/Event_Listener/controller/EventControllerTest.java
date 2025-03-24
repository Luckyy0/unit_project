package com.example.Event_Listener.controller;

import com.example.Event_Listener.annotation.Logable;
import com.example.Event_Listener.dto.UserEventObject;
import com.example.Event_Listener.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventControllerTest {
    private final UserEventService userEventService;

    @PostMapping("/register_user")
    @Logable(value = "Created user log", logParams = true, logResult = true)
    public void eventRegisterUser(@RequestBody UserEventObject userEventObject) {
        userEventService.registerCustomer(userEventObject);
    }
}
