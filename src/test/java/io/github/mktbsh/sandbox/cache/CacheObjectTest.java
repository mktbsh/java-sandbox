package io.github.mktbsh.sandbox.cache;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;

class CacheObjectTest {

    @Test
    @DisplayName("データを生成時と等しい型・値で取得できる")
    void getData() {
        TimeToLive ttl = TimeToLive.days(1);

        CacheObject<String> stringCache = new CacheObject<>("data", ttl);
        CacheObject<Integer> intCache = new CacheObject<>(100_000, ttl);
        CacheObject<Boolean> boolCache = new CacheObject<>(true, ttl);
        CacheObject<TimeToLive> ttlCache = new CacheObject<>(ttl, ttl);

        assertEquals("data", stringCache.getData());
        assertEquals(100_000, intCache.getData());
        assertTrue(boolCache.getData());
        assertEquals(86_400, ttlCache.getData().getTTL());
    }

    @Test
    @DisplayName("現在時刻が有効期限以前の場合、isAvailableはtrue, isExpiredはfalseを返す")
    void isAvailable() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            LocalDateTime now = LocalDateTime.parse("2023-07-18T10:00:00");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            CacheObject<String> cacheObject = new CacheObject<>("data", TimeToLive.seconds(30));

            assertTrue(cacheObject.isAvailable());
            assertFalse(cacheObject.isExpired());
        }
    }

    @Test
    @DisplayName("現在時刻が有効期限以降の場合、isAvailableはfalse, isExpiredはtrueを返す")
    void isExpired() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            LocalDateTime now = LocalDateTime.parse("2023-07-18T10:00:00");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            CacheObject<String> cacheObject = new CacheObject<>("data", TimeToLive.minutes(-30));

            assertFalse(cacheObject.isAvailable());
            assertTrue(cacheObject.isExpired());
        }
    }
}
