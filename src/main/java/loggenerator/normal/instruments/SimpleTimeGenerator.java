package loggenerator.normal.instruments;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleTimeGenerator {
    private static LocalTime base;

    static {
        base = LocalTime.now();
    }

    public static LocalTime generate() {
        long nanos = generateRandomNanos();
        base = base.plusNanos(nanos);
        return base;
    }

    public static LocalTime generateFrom(LocalTime otherBase) {
        long nanos = generateRandomNanos();
        return otherBase.plusNanos(nanos);
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
