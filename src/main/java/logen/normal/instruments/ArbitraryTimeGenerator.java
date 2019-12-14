package logen.normal.instruments;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.ThreadLocalRandom;

public class ArbitraryTimeGenerator {

    private ArbitraryTimeGenerator() {

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
