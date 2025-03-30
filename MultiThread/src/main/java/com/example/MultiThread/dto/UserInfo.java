package com.example.MultiThread.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String name;
    private String email;

    public UserInfo(String name) {
        this.name = name;
        this.email = "%s@gmail.com".formatted(name);
    }
}
