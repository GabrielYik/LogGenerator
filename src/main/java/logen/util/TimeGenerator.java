package logen.util;

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class TimeGenerator {
    private TimeGenerator() {

    }

    public static LocalTime generateFrom(LocalTime otherBase) {
        long seconds = generateRandomSeconds();
        otherBase = otherBase.plusSeconds(seconds);
        return otherBase;
    }

    private static long generateRandomSeconds() {
        return ThreadLocalRandom
            .current()
            .nextLong(10, 100);
    }
}
