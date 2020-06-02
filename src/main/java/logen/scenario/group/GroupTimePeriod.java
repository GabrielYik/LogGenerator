package logen.scenario.group;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.util.Pair;
import logen.scenario.common.LocalTimeDeserialiser;
import logen.scenario.time.TimePeriod;
import logen.util.RandomUtil;

import java.time.LocalTime;

public class GroupTimePeriod {
    private static final GroupTimePeriodType DEFAULT_TYPE = GroupTimePeriodType.ANY;

    private GroupTimePeriodType type;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime startTime;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime endTime;

    public static void setAttributesIfAbsent(GroupTimePeriod timePeriod, TimePeriod globalTimePeriod) {
        if (timePeriod == null) {
            timePeriod = new GroupTimePeriod();
        }

        if (timePeriod.type == null && timePeriod.startTime == null && timePeriod.endTime == null) {
            timePeriod.type = DEFAULT_TYPE;
        }
        if (timePeriod.type == null && (timePeriod.startTime != null || timePeriod.endTime != null)) {
            timePeriod.type = GroupTimePeriodType.CUSTOM;
        }
        handleSystemChoices(timePeriod, globalTimePeriod);
    }

    private static void handleSystemChoices(GroupTimePeriod timePeriod, TimePeriod globalTimePeriod) {
        LocalTime startTime;
        LocalTime endTime;
        switch(timePeriod.getType()) {
            case ANY:
                startTime = RandomUtil.chooseBetween(
                        globalTimePeriod.getStartTime(),
                        globalTimePeriod.getEndTime().minusHours(2)
                );
                endTime = startTime.plusHours(2);
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case CUSTOM:
                // do nothing
                break;
            case ONE_HOUR:
                startTime = RandomUtil.chooseBetween(
                        globalTimePeriod.getStartTime(),
                        globalTimePeriod.getEndTime().minusHours(1)
                );
                endTime = startTime.plusHours(1);
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case ONE_DAY:
                startTime = globalTimePeriod.getStartTime();
                endTime = globalTimePeriod.getEndTime();
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case AFTER_MIDNIGHT:
                Pair<LocalTime, LocalTime> startEndTime = GroupTimePeriodType.map(GroupTimePeriodType.AFTER_MIDNIGHT);
                startTime = startEndTime.getKey();
                endTime = startEndTime.getValue();
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            default:
                throw new AssertionError();
        }
    }

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
