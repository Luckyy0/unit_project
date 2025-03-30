package com.example.MultiThread.cache;

import com.example.MultiThread.cache.annotation.UserCache;
import com.example.MultiThread.cache.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public abstract class UserCacheCustomManager<K, V> implements CacheCustomManager<K, V> {
    @Autowired
    @Qualifier(value = CacheConstant.USER_CACHE_BEAN_NAME)
    protected CacheManager cacheManager;

    public Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new RuntimeException("Cache not found");
        };
        return cache;
    }
}
