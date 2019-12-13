package loggenerator.normal.instruments;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.ThreadLocalRandom;

public class LateNightTimeGenerator {
    private static LocalTime base;

    static {
        base = LocalTime.MIDNIGHT.plusHours(1);
    }

    public static LocalTime generate() {
        Instant start = Instant.now().minus(Duration.ofNanos(100));
        Instant end = Instant.now().minus(Duration.ofNanos(10));
        long startSeconds = start.getLong(ChronoField.NANO_OF_SECOND);
        long endSeconds = end.getLong(ChronoField.NANO_OF_SECOND);
        long random = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds);
        base = base.plusNanos(random);
        return base;
    }
}
