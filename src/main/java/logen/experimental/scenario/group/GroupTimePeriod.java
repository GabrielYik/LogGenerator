package logen.experimental.scenario.group;

import java.time.LocalTime;

public class GroupTimePeriod {
    private GroupTimePeriodType type;
    private LocalTime startTime;
    private LocalTime endTime;

    public GroupTimePeriodType getType() {
        return type;
    }

    public void setType(GroupTimePeriodType type) {
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

    public void setStartEndTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
