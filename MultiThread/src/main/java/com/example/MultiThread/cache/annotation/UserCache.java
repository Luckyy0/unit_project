package com.example.MultiThread.cache.annotation;

import com.example.MultiThread.cache.constant.CacheConstant;
import org.springframework.cache.annotation.CacheConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@CacheConfig(cacheManager = CacheConstant.USER_CACHE_BEAN_NAME)
public @interface UserCache {
}
