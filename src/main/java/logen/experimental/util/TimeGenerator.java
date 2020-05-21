package logen.experimental.util;

import logen.experimental.scenario.TimePeriod;

import java.time.LocalTime;

public class TimeGenerator {
    private final LocalTime earliestTime;
    private final LocalTime latestTime;
    private final int approximateRequiredGenerations;

    public TimeGenerator(TimePeriod timePeriod, int approximateRequiredGenerations) {
        this.earliestTime = timePeriod.getStartTime();
        this.latestTime = timePeriod.getEndTime();
        this.approximateRequiredGenerations = approximateRequiredGenerations;
    }

    public TimeGenerator(
            LocalTime earliestTime,
            LocalTime latestTime,
            int approximateRequiredGenerations
    ) {
        this.earliestTime = earliestTime;
        this.latestTime = latestTime;
        this.approximateRequiredGenerations = approximateRequiredGenerations;
    }

    public LocalTime generate() {
        return null;
    }
}
