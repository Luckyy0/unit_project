package com.example.MultiThread.service;

import com.example.MultiThread.cache.CacheType;
import com.example.MultiThread.dto.UserInfo;
import com.example.MultiThread.gate.FakeGetUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final FakeGetUserApi fakeGetUserApi;
    private final Executor taskExecutor;
    private final int BATCH_SIZE = 2;
    private final List<String> users = List.of("user1", "user2", "user3", "user4", "user5");

    // Get list user info order by input list
    public List<UserInfo> getListUserInfo() {
        //Create Map<userId, userInfo>
        Map<String, UserInfo> userMap = new LinkedHashMap<>();
        users.forEach(key -> userMap.put(key, null));
        //Get User from cache
        List<String> userIdsNotInCache = getUserInfoCache(userMap);
        //Get User from external API by batch (if external api support)
        List<List<String>> batch = getBatchUserInfo(userIdsNotInCache);
        List<CompletableFuture<List<UserInfo>>> futures = batch.stream()
                .map(userList -> CompletableFuture.supplyAsync(() -> {
                            return processUser(userList);
                        }
                        , taskExecutor))
                .toList();
        // CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures.stream().map(CompletableFuture::join).flatMap(List::stream).toList().forEach(user -> userMap.put(user.getName(), user));
        return userMap.values().stream().toList();
    }

    @SuppressWarnings("unchecked")
    public UserInfo getUserInfo() {
        fakeGetUserApi.getUser("user6");
        return (UserInfo) CacheType.USER_CACHE_INFO.getCacheCustomManager().getFromCache("user6");
    }

    @SuppressWarnings("unchecked")
    private List<String> getUserInfoCache(Map<String, UserInfo> userMap) {
        List<String> userRes = new ArrayList<>();
        users.forEach(userId -> {
            UserInfo userCache = (UserInfo) CacheType.USER_CACHE_INFO.getCacheCustomManager().getFromCache(userId);
            if (userCache != null) {
                userMap.put(userId, userCache);
            } else {
                userRes.add(userId);
            }
        });
        return userRes;
    }

    private List<List<String>> getBatchUserInfo(List<String> users) {
        return IntStream.range(0, (int) Math.ceil(users.size() / (double) BATCH_SIZE))
                .mapToObj(i -> users.subList(i * BATCH_SIZE, Math.min((i + 1) * BATCH_SIZE, users.size())))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private List<UserInfo> processUser(List<String> userIdList) {
        List<UserInfo> userAddToCache = fakeGetUserApi.getUserList(userIdList);
        userAddToCache.forEach(user -> CacheType.USER_CACHE_INFO.getCacheCustomManager().addToCache(user.getName(), user));
        return userAddToCache;
    }
}
