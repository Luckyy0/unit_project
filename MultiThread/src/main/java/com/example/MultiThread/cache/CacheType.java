package com.example.MultiThread.cache;

import com.example.MultiThread.cache.cacheManager.ProductCache;
import com.example.MultiThread.cache.cacheManager.UserinfoCache;
import com.example.MultiThread.cache.constant.CacheConstant;
import com.example.MultiThread.cache.cacheManager.UserPositionCache;
import com.example.MultiThread.config.ApplicationContextHolder;

public enum CacheType {
    USER_CACHE_INFO(CacheConstant.USER_CACHE_INFO, UserinfoCache.class),
    USER_CACHE_POSITION(CacheConstant.USER_CACHE_POSITION, UserPositionCache.class),
    PRODUCT_CACHE(CacheConstant.PRODUCT_CACHE, ProductCache.class);

    public final String cacheName;
    @SuppressWarnings("rawtypes")
    public final Class<? extends CacheCustomManager> cacheManagerClass;

    @SuppressWarnings("rawtypes")
    CacheType(String cacheName, Class<? extends CacheCustomManager> cacheManagerClass) {
        this.cacheName = cacheName;
        this.cacheManagerClass = cacheManagerClass;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends CacheCustomManager> T getCacheCustomManager() {
        return (T) ApplicationContextHolder.getContext().getBean(this.cacheManagerClass);
    }
}
