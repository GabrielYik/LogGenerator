package logen.experimental.generation.fixed;

import javafx.util.Pair;

import java.time.LocalTime;

public enum TimePeriodType {
    ONE_HOUR(null, null),
    ONE_DAY(null, null),
    AFTER_MIDNIGHT(LocalTime.of(0, 0), LocalTime.of(3, 3));

    private final LocalTime startTime;
    private final LocalTime endTime;

    TimePeriodType(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Pair<LocalTime, LocalTime> map(TimePeriodType type) {
        if (type.equals(AFTER_MIDNIGHT)) {
            return new Pair<>(AFTER_MIDNIGHT.startTime, AFTER_MIDNIGHT.endTime);
        } else {
            throw new AssertionError();
        }
    }
}
