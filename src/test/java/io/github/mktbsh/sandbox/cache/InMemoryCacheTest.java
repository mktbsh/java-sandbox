package io.github.mktbsh.sandbox.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCacheTest {

    @Test
    @DisplayName("InMemoryCacheオブジェクトを生成する")
    void testValidConstructor() {
        assertDoesNotThrow(() -> new InMemoryCache<String>(TimeToLive.seconds(30)));
    }

    @Test
    @DisplayName("InMemoryCacheオブジェクトを生成時にTTLオブジェクトがnullの場合は例外が発生する")
    void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new InMemoryCache<String>(null));
    }

    @Test
    @DisplayName("キャッシュ取得: キーに対応するデータが存在しない場合")
    void testGetEmptyData() {
        InMemoryCache<String> cache = new InMemoryCache<>(TimeToLive.hour(30));

        assertEquals(Optional.empty(), cache.get("unknown-cache-key"));
    }

    @Test
    @DisplayName("キャッシュ取得: 有効期限切れ")
    void testGetExpiredData() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            // Mock
            LocalDateTime now = LocalDateTime.parse("2023-07-18T10:00:00");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            // Arrange
            String cacheKey = "cache-key";
            InMemoryCache<String> cache = new InMemoryCache<>(TimeToLive.minutes(-30));
            cache.put(cacheKey, "this is expired cache data");

            // Act
            Optional<String> data = cache.get(cacheKey);

            // Assert
            assertEquals(Optional.empty(), data);
        }
    }

    @Test
    @DisplayName("キャッシュ取得: 有効期限内のデータを取得")
    void testGetFullyData() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            // Mock
            LocalDateTime now = LocalDateTime.parse("2023-07-18T10:00:00");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            // Arrange
            InMemoryCache<String> cache = new InMemoryCache<>(TimeToLive.minutes(30));

            String cacheKey = "cache-key";
            String cacheData = "this is available cache data";
            cache.put(cacheKey, cacheData);

            // Act
            Optional<String> data = cache.get(cacheKey);

            // Assert
            assertEquals(cacheData, data.orElse("empty"));
        }
    }

    @Test
    @DisplayName("生成からPurgeまでオペレーションテスト")
    void cacheOperation() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            LocalDateTime now = LocalDateTime.parse("2023-07-17T10:15:30");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            InMemoryCache<String> cache = new InMemoryCache<>(TimeToLive.seconds(30));

            // Test put
            cache.put("key", "value");
            Optional<String> cachedValue = cache.get("key");
            assertTrue(cachedValue.isPresent());
            assertEquals("value", cachedValue.get());

            // Test get for expired value
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now.plusSeconds(31));
            cachedValue = cache.get("key");
            assertFalse(cachedValue.isPresent());

            // Test remove
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);
            cache.put("key", "value");
            cache.remove("key");
            cachedValue = cache.get("key");
            assertFalse(cachedValue.isPresent());

            // Test purge
            cache.put("key", "value");
            cache.purge();
            cachedValue = cache.get("key");
            assertFalse(cachedValue.isPresent());

            // Test put 2nd
            cache.put("key", "value");
            Optional<String> cachedValue2 = cache.get("key");
            assertTrue(cachedValue2.isPresent());
            assertEquals("value", cachedValue2.get());
        }
    }
}