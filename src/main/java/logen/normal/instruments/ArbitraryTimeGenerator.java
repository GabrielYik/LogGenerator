package logen.normal.instruments;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.ThreadLocalRandom;

public class ArbitraryTimeGenerator {
    private static LocalTime base;
    private static LocalTime earlyMorning;
    private static LocalTime evening;

    static {
        base = LocalTime.now();
        earlyMorning = LocalTime.MIDNIGHT.plusHours(2);
        evening = LocalTime.NOON.plusHours(6);
    }

    private ArbitraryTimeGenerator() {

    }

    public static LocalTime generateRandomTime() {
        return generateFrom(base);
    }

    public static LocalTime generateEarlyMorningTime() {
        return generateFrom(earlyMorning);
    }

    public static LocalTime generateEveningTime() {
        return generateFrom(evening);
    }

    public static LocalTime generateFrom(LocalTime otherBase) {
        long nanos = generateRandomNanos();
        otherBase = otherBase.plusNanos(nanos);
        return otherBase;
    }

    private static long generateRandomNanos() {
        Instant start = Instant.now().minus(Duration.ofNanos(100));
        Instant end = Instant.now().minus(Duration.ofNanos(10));
        long startSeconds = start.getLong(ChronoField.NANO_OF_SECOND);
        long endSeconds = end.getLong(ChronoField.NANO_OF_SECOND);
        return ThreadLocalRandom
            .current()
            .nextLong(Math.min(startSeconds, endSeconds), Math.max(startSeconds, endSeconds));
    }

}
