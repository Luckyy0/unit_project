package com.example.MultiThread.config;

import com.example.MultiThread.cache.constant.CacheConstant;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = CacheConstant.USER_CACHE_BEAN_NAME)
    @Primary
    public CacheManager userCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                CacheConstant.USER_CACHE_POSITION,
                CacheConstant.USER_CACHE_INFO
        );
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(500)
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .expireAfterAccess(30, TimeUnit.MINUTES)
                        .recordStats()
        );
        return cacheManager;
    }

    @Bean(name = CacheConstant.PRODUCT_CACHE_BEAN_NAME)
    public CacheManager productCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CacheConstant.PRODUCT_CACHE);
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .initialCapacity(400)
                        .maximumSize(600)
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .expireAfterAccess(30, TimeUnit.MINUTES)
                        .recordStats()
        );
        return cacheManager;
    }
}
