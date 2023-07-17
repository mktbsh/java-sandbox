package io.github.mktbsh.sandbox.cache;

import java.time.LocalDateTime;

public class TimeToLive {

    private final int ttlSeconds;

    private TimeToLive(final int seconds) {
        this.ttlSeconds = seconds;
    }

    public static TimeToLive seconds(final int seconds) {
        return new TimeToLive(seconds);
    }

    public static TimeToLive minutes(final int minutes) {
        return new TimeToLive(minutes * 60);
    }


    public static TimeToLive hour(final int hour) {
        return minutes(hour * 60);
    }

    public static TimeToLive days(final int days) {
        return hour(days * 24);
    }

    public int getTTL() {
        return ttlSeconds;
    }

    public LocalDateTime getExpiryTime() {
        return LocalDateTime.now().plusSeconds(ttlSeconds);
    }

}