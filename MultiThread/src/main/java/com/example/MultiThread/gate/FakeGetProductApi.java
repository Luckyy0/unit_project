package com.example.MultiThread.gate;

import com.example.MultiThread.cache.annotation.ProductCache;
import com.example.MultiThread.cache.constant.CacheConstant;
import com.example.MultiThread.dto.Product;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ProductCache
public class FakeGetProductApi {
    
    @RateLimiter(name = "externalApiRateLimiter", fallbackMethod = "fallbackGetProducts")
    public List<Product> getProductList(List<String> productIds) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  productIds.stream().map(Product::new).toList();
    }

    // Hàm fallback khi bị giới hạn
    public List<String> fallbackGetProducts(Throwable t) {
        return List.of("Fallback: Too many requests");
    }

    @Cacheable(value = CacheConstant.PRODUCT_CACHE, key = "#productId")
    public Product getProduct(String productId) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Product(productId);
    }
}
