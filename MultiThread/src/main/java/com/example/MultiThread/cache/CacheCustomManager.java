package com.example.MultiThread.cache;

public interface CacheCustomManager<K, V> {
    void addToCache(K key, V value);
    void removeFromCache(K key);
    V getFromCache(K key);
    void clearCache();
}
