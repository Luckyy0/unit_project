package com.example.jsonUtil.controller;

import com.example.jsonUtil.Domain.User;
import com.example.jsonUtil.info.UserInfo;
import com.example.jsonUtil.info.UserResponse;
import com.example.jsonUtil.mysqlService.UserMysqlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
    private final UserMysqlService userMysqlService;

    @PostMapping()
    public UserResponse createUser(@RequestBody UserInfo userInfo) {
        return userMysqlService.insert(userInfo).toResponse();
    }

    @PutMapping("/{userId}")
    public UserResponse updatedUser(@PathVariable("userId") Integer userId,@RequestBody UserInfo userInfo) {
        userInfo.setUserId(userId);
        return userMysqlService.update(userInfo).toResponse();
    }

    @GetMapping("/change_json_data")
    public UserResponse changeJsonData() {
        return userMysqlService.changJsonData().toResponse();
    }

    @DeleteMapping("/{userId}")
    public boolean deletedUser(@PathVariable("userId") Integer userId) {
        return userMysqlService.deleteUser(userId);
    }

    @GetMapping("/{userId}")
    public UserResponse findUser(@PathVariable("userId") Integer userId) {
        return userMysqlService.findById(userId).toResponse();
    }

    @GetMapping()
    public String testConnection() {
        return "connect successfully";
    }
}
