package logen.scenario.time;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import logen.scenario.common.LocalTimeDeserialiser;

import java.time.LocalTime;

public class TimePeriod {
    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(9, 0, 0);
    private static final LocalTime DEFAULT_END_TIME = LocalTime.of(17, 0, 0);

    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime startTime;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime endTime;

    /**
     * Constructs a default instance of {@code timePeriod} if null
     * and sets property values to their default values if absent.
     *
     * @param timePeriod A time period
     */
    public static void setAttributesIfAbsent(TimePeriod timePeriod) {
        if (timePeriod == null) {
            timePeriod = new TimePeriod();
        }
        if (timePeriod.startTime == null) {
            timePeriod.startTime = DEFAULT_START_TIME;
        }
        if (timePeriod.endTime == null) {
            timePeriod.endTime = DEFAULT_END_TIME;
        }
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
