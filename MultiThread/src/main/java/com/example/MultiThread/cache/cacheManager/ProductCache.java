package com.example.MultiThread.cache.cacheManager;

import com.example.MultiThread.cache.CacheCustomManager;
import com.example.MultiThread.cache.CacheType;
import com.example.MultiThread.cache.ProductCacheCustomManager;
import com.example.MultiThread.cache.constant.CacheConstant;
import com.example.MultiThread.dto.Product;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ProductCache extends ProductCacheCustomManager<String, Product> {
    private Cache productCache;

    @PostConstruct
    public void init() {
        productCache = getCache(CacheConstant.PRODUCT_CACHE);
    }

    @Override
    public void addToCache(String key, Product value) {
        productCache.put(key, value);
    }

    @Override
    public void removeFromCache(String key) {
        productCache.evict(key);
    }

    @Override
    public Product getFromCache(String key) {
        return productCache.get(key, Product.class);
    }

    @Override
    public void clearCache() {
        productCache.clear();
    }
}
