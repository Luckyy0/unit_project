package com.example.MultiThread.controller;

import com.example.MultiThread.dto.UserInfo;
import com.example.MultiThread.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping("/list")
    public List<UserInfo> getListUser() {
        return userService.getListUserInfo();
    }

    @RequestMapping()
    public UserInfo getUser() {
        return userService.getUserInfo();
    }
}
