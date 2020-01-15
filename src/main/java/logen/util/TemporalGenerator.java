package logen.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class TemporalGenerator {
    private static int SECONDS_IN_MINUTE = 60;
    private static int SECONDS_IN_HOUR = 3600;
    private static int SECONDS_IN_DAY = 86400;

    private TemporalGenerator() {

    }

    public static LocalTime generateTimeFrom(LocalTime base) {
        long seconds = generateRandomSeconds(SECONDS_IN_MINUTE, SECONDS_IN_HOUR);
        return base.plusSeconds(seconds);
    }

    public static LocalDateTime generateDateTimeFrom(LocalDateTime base) {
        long seconds = generateRandomSeconds(SECONDS_IN_DAY, SECONDS_IN_DAY + SECONDS_IN_HOUR);
        return base.plusSeconds(seconds);
    }

    private static long generateRandomSeconds(long origin, long bound) {
        return ThreadLocalRandom
            .current()
            .nextLong(origin, bound);
    }
}
