package logen.experimental.scenario;

import logen.experimental.generation.fixed.TimePeriodType;

import java.time.LocalTime;

public class TimePeriod {
    private TimePeriodType type;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimePeriodType getType() {
        return type;
    }

    public void setType(TimePeriodType type) {
        this.type = type;
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
