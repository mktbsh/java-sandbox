package io.github.mktbsh.sandbox.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCache<T> {
    /**
     * キャッシュの有効期限
     */
    private final TimeToLive ttl;

    /**
     * キャッシュデータ
     */
    private final ConcurrentHashMap<String, CacheObject<T>> cache;

    public InMemoryCache(final TimeToLive ttl) {
        if (ttl == null) throw new IllegalArgumentException("TTLオブジェクトは必須です。");

        this.ttl = ttl;
        this.cache = new ConcurrentHashMap<>();
    }

    public Optional<T> get(String key) {
        final CacheObject<T> obj = cache.get(key);

        if (obj == null) return Optional.empty();

        if (obj.isExpired()) {
            return Optional.empty();
        }

        return Optional.of(obj.getData());
    }

    public void put(String key, T value) {
        cache.put(key, new CacheObject<>(value, ttl));
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void purge() {
        cache.clear();
    }
}