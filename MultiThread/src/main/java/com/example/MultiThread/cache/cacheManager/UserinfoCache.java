package com.example.MultiThread.cache.cacheManager;

import com.example.MultiThread.cache.CacheType;
import com.example.MultiThread.cache.UserCacheCustomManager;
import com.example.MultiThread.cache.constant.CacheConstant;
import com.example.MultiThread.dto.UserInfo;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserinfoCache extends UserCacheCustomManager<String, UserInfo> {
    private Cache userCache;

    @PostConstruct
    public void init() {
        userCache = getCache(CacheConstant.USER_CACHE_INFO);
    }

    @Override
    public void addToCache(String key, UserInfo value) {
        userCache.put(key, value);
    }

    @Override
    public void removeFromCache(String key) {
        userCache.evict(key);
    }

    @Override
    public UserInfo getFromCache(String key) {
        return userCache.get(key, UserInfo.class);
    }

    @Override
    public void clearCache() {
        userCache.clear();
    }
}

