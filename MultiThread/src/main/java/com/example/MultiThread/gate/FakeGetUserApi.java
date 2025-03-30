package com.example.MultiThread.gate;

import com.example.MultiThread.cache.annotation.UserCache;
import com.example.MultiThread.cache.constant.CacheConstant;
import com.example.MultiThread.dto.UserInfo;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@UserCache
public class FakeGetUserApi {

    @RateLimiter(name = "externalApiRateLimiter", fallbackMethod = "getUserListFallback")
    public List<UserInfo> getUserList(List<String> userIds) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userIds.stream().map(UserInfo::new).toList();
    }

    // Hàm fallback khi bị giới hạn
    public List<String> getUserListFallback(Throwable t) {
        return List.of("Fallback: Too many requests");
    }

    @Cacheable(value = CacheConstant.USER_CACHE_INFO, key = "#userid")
    public UserInfo getUser(String userid) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new UserInfo(userid);
    }
}
