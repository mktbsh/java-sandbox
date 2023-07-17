package io.github.mktbsh.sandbox.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeToLiveTest {

    @Test
    @DisplayName("生存期間30秒のTTLオブジェクトを生成")
    void seconds() {
        TimeToLive ttl = TimeToLive.seconds(30);
        assertEquals(30, ttl.getTTL());
    }

    @Test
    @DisplayName("生存期間2分間のTTLオブジェクトを生成")
    void minutes() {
        TimeToLive ttl = TimeToLive.minutes(2);
        assertEquals(120, ttl.getTTL());
    }

    @Test
    @DisplayName("生存期間3時間のTTLオブジェクトを生成")
    void hour() {
        TimeToLive ttl = TimeToLive.hour(3);
        assertEquals(10800, ttl.getTTL());
    }

    @Test
    @DisplayName("生存期間1日のTTLオブジェクトを生成")
    void days() {
        TimeToLive ttl = TimeToLive.days(1);
        assertEquals(86400, ttl.getTTL());
    }

    @Test
    @DisplayName("TTLオブジェクトの値を取得")
    void getTTl() {
        TimeToLive ttl = TimeToLive.seconds(30);
        assertEquals(30, ttl.getTTL());
    }

    @Test
    @DisplayName("秒ベースでTTLを生成し、有効期限を取得")
    void getExpiryTime_seconds() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            LocalDateTime now = LocalDateTime.parse("2023-07-17T10:15:30");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            TimeToLive ttl = TimeToLive.seconds(30);

            LocalDateTime expectedExpiryTime = LocalDateTime.parse("2023-07-17T10:16:00");
            assertEquals(expectedExpiryTime, ttl.getExpiryTime());
        }
    }

    @Test
    @DisplayName("分ベースでTTLを生成し、有効期限を取得")
    void getExpiryTime_minutes() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            LocalDateTime now = LocalDateTime.parse("2023-07-17T10:15:30");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            TimeToLive ttl = TimeToLive.minutes(30);

            LocalDateTime expectedExpiryTime = LocalDateTime.parse("2023-07-17T10:45:30");
            assertEquals(expectedExpiryTime, ttl.getExpiryTime());
        }
    }

    @Test
    @DisplayName("時間ベースでTTLを生成し、有効期限を取得")
    void getExpiryTime_hours() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            LocalDateTime now = LocalDateTime.parse("2023-07-17T10:00:00");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            TimeToLive ttl = TimeToLive.hour(5);

            LocalDateTime expectedExpiryTime = LocalDateTime.parse("2023-07-17T15:00:00");
            assertEquals(expectedExpiryTime, ttl.getExpiryTime());
        }
    }

    @Test
    @DisplayName("日数ベースでTTLを生成し、有効期限を取得")
    void getExpiryTime_days() {
        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            LocalDateTime now = LocalDateTime.parse("2023-07-17T10:00:00");
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            TimeToLive ttl = TimeToLive.days(3);

            LocalDateTime expectedExpiryTime = LocalDateTime.parse("2023-07-20T10:00:00");
            assertEquals(expectedExpiryTime, ttl.getExpiryTime());
        }
    }

}