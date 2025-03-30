package com.example.MultiThread.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String name;
    private String description;
    private double price;
    private int quantity;

    public Product(String name) {
        this.name = name;
        this.description = "No description";
        this.price = 0.0;
        this.quantity = 10;
    }
}
