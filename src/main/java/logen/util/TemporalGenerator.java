package logen.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class TemporalGenerator {
    private static int SECONDS_IN_MINUTE = 60;
    private static int SECONDS_IN_HOUR = 3600;
    private static int SECONDS_IN_DAY = 86400;

    private LocalDateTime base;
    private LocalDateTime activeStart;
    private LocalDateTime activeEnd;

    public TemporalGenerator(LocalDateTime activeStart, LocalDateTime activeEnd) {
        base = activeStart;
        this.activeStart = activeStart;
        this.activeEnd = activeEnd;
    }

    public LocalTime generateTime() {
        long seconds = generateRandomSeconds(SECONDS_IN_MINUTE, SECONDS_IN_HOUR);
        LocalDateTime newBase = base.plusSeconds(seconds);
        if (newBase.isAfter(activeEnd)) {
            base = activeStart;
            return generateTime();
        } else {
            base = newBase;
            return newBase.toLocalTime();
        }
    }

    private static long generateRandomSeconds(long origin, long bound) {
        return ThreadLocalRandom
            .current()
            .nextLong(origin, bound);
    }

    public void changeBase(LocalTime base) {
        this.base = LocalDateTime.from(base);
    }
}
