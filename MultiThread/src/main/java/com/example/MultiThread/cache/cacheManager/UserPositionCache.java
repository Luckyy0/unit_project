package com.example.MultiThread.cache.cacheManager;

import com.example.MultiThread.cache.CacheType;
import com.example.MultiThread.cache.UserCacheCustomManager;
import com.example.MultiThread.cache.constant.CacheConstant;
import com.example.MultiThread.dto.UserPosition;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserPositionCache extends UserCacheCustomManager<String, UserPosition> {
    private Cache userCache;

    @PostConstruct
    public void init() {
        userCache = getCache(CacheConstant.USER_CACHE_POSITION);
    }

    @Override
    public void addToCache(String key, UserPosition value) {
        userCache.put(key, value);
    }

    @Override
    public void removeFromCache(String key) {
        userCache.evict(key);
    }

    @Override
    public UserPosition getFromCache(String key) {
        return userCache.get(key, UserPosition.class);
    }

    @Override
    public void clearCache() {
        userCache.clear();
    }
}

