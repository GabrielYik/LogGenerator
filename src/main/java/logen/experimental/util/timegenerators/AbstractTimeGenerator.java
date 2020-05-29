package logen.experimental.util.timegenerators;

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractTimeGenerator implements TimeGenerator {
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOUR = 3600;

    protected final LocalTime fromTime;
    protected final LocalTime wrapAroundTime;
    protected final LocalTime baseTime;

    protected LocalTime currentTime;

    protected AbstractTimeGenerator(
            LocalTime fromTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        this.fromTime = fromTime;
        this.wrapAroundTime = wrapAroundTime;
        this.baseTime = baseTime;
        this.currentTime = fromTime;
    }

    @Override
    public abstract LocalTime generate();

    protected static long generateRandomSeconds() {
        return ThreadLocalRandom
                .current()
                .nextLong(SECONDS_IN_MINUTE, SECONDS_IN_HOUR);
    }

    public static int computeAverageInterval() {
        return SECONDS_IN_HOUR - SECONDS_IN_MINUTE;
    }
}
