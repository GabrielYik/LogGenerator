package logen.experimental.scenario.time;

import java.time.LocalTime;
import java.util.List;

public class TimePeriod {
    private TimePeriodType type;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<TimePeriodSpecific> specifics;

    public TimePeriod() {

    }

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

    public List<TimePeriodSpecific> getSpecifics() {
        return specifics;
    }

    public void setSpecifics(List<TimePeriodSpecific> specifics) {
        this.specifics = specifics;
    }
}
