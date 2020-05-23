package logen.experimental.util;

import logen.experimental.scenario.TimePeriod;

import java.sql.Time;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class TimeGenerator {
    private static int SECONDS_IN_MINUTE = 60;
    private static int SECONDS_IN_HOUR = 3600;

    private final LocalTime earliestTime;
    private final LocalTime latestTime;
    private final int approximateRequiredGenerations;

    private LocalTime base;

    private TimeGenerator(
            LocalTime earliestTime,
            LocalTime latestTime,
            int approximateRequiredGenerations
    ) {
        this.earliestTime = earliestTime;
        this.latestTime = latestTime;
        this.approximateRequiredGenerations = approximateRequiredGenerations;
    }

    public static TimeGenerator bounded(
            LocalTime earliestTime,
            LocalTime latestTime,
            int approximateRequiredGenerations
    ) {
        return new TimeGenerator(earliestTime, latestTime, approximateRequiredGenerations);
    }

    public static TimeGenerator forward(LocalTime earliestTime) {
        return new TimeGenerator(earliestTime, null, -1);
    }

    public static TimeGenerator back(LocalTime latestTime) {
        return new TimeGenerator(null, latestTime, -1);
    }

    public LocalTime generate() {
        long seconds = generateRandomSeconds(SECONDS_IN_MINUTE, SECONDS_IN_HOUR);
        boolean isBack = earliestTime == null && latestTime != null;
        if (isBack) {
            base = base.minusSeconds(seconds);
        } else {
            base = base.plusSeconds(seconds);
        }
        return base;
    }

    private static long generateRandomSeconds(long origin, long bound) {
        return ThreadLocalRandom
                .current()
                .nextLong(origin, bound);
    }


    public void skip(int generationCount) {

    }
}
