package io.github.mktbsh.sandbox.cache;

import java.time.LocalDateTime;

public class CacheObject<T> {

    private final T data;
    private final LocalDateTime expiryTime;

    public CacheObject(final T data, final TimeToLive ttl) {
        this.data = data;
        this.expiryTime = ttl.getExpiryTime();
    }

    public T getData() {
        return data;
    }

    public boolean isAvailable() {
        final LocalDateTime now = LocalDateTime.now();
        return now.isBefore(expiryTime);
    }

    public boolean isExpired() {
        return !isAvailable();
    }

}