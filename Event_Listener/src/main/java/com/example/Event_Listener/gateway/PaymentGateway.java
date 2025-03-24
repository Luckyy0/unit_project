package com.example.Event_Listener.gateway;

import java.util.Random;

public class PaymentGateway {
    public static boolean processPayment() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
