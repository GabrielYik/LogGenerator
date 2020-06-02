package logen.experimental.scenario.time;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import logen.experimental.scenario.common.LocalTimeDeserialiser;
import java.time.LocalTime;

public class TimePeriod {
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime startTime;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime endTime;

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
