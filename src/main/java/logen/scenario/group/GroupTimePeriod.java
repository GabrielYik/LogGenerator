package logen.scenario.group;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import logen.scenario.common.LocalTimeDeserialiser;
import logen.scenario.time.TimePeriod;
import logen.util.RandomUtil;

import java.time.LocalTime;

public class GroupTimePeriod {
    private static final GroupTimePeriodType DEFAULT_TYPE = GroupTimePeriodType.ANY;
    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(9, 0, 0);
    private static final LocalTime DEFAULT_END_TIME = LocalTime.of(17, 0, 0);

    private static final int ONE_HOUR_OFFSET = 1;

    private static final LocalTime DEFAULT_AFTER_MIDNIGHT_START_TIME = LocalTime.of(1, 0, 0);
    private static final LocalTime DEFAULT_AFTER_MIDNIGHT_END_TIME = LocalTime.of(4, 0, 0);

    private GroupTimePeriodType type;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime startTime;
    @JsonDeserialize(using = LocalTimeDeserialiser.class)
    private LocalTime endTime;

    /**
     * Sets the attribute or sub-attributes of {@code timePeriod} if any
     * are missing.
     *
     * At this point, there are 4 cases of missing sub-attributes that can
     * be handled:
     * <ol>
     *     <li>Type, start time and end time
     *     <li>Type, and start time or end time
     *     <li>Given type of {@code CUSTOM}, start time or end time
     *     <li>Given type of not {@code CUSTOM}, start and end time
     * </ol>
     *
     * @param timePeriod The attribute
     * @param globalTimePeriod The time period for the log sheet
     */
    public static void setAttributesIfAbsent(GroupTimePeriod timePeriod, TimePeriod globalTimePeriod) {
        if (timePeriod == null) {
            timePeriod = new GroupTimePeriod();
        }

        if (timePeriod.type == null) {
            if (timePeriod.startTime == null && timePeriod.endTime == null) {
                timePeriod.type = DEFAULT_TYPE;
            } else {
                timePeriod.type = GroupTimePeriodType.CUSTOM;
            }
        }

        if (timePeriod.type.equals(GroupTimePeriodType.CUSTOM)) {
            if (timePeriod.startTime == null) {
                timePeriod.startTime = DEFAULT_START_TIME;
            }
            if (timePeriod.endTime == null) {
                timePeriod.endTime = DEFAULT_END_TIME;
            }
        }

        handleSystemChoices(timePeriod, globalTimePeriod);
    }

    private static void handleSystemChoices(GroupTimePeriod timePeriod, TimePeriod globalTimePeriod) {
        LocalTime startTime;
        LocalTime endTime;
        switch(timePeriod.getType()) {
            case ANY:
                timePeriod.setStartEndTime(DEFAULT_START_TIME, DEFAULT_END_TIME);
                break;
            case CUSTOM:
                // do nothing
                break;
            case ONE_HOUR:
                startTime = RandomUtil.chooseBetweenInclusive(
                        globalTimePeriod.getStartTime(),
                        globalTimePeriod.getEndTime().minusHours(ONE_HOUR_OFFSET)
                );
                endTime = startTime.plusHours(ONE_HOUR_OFFSET);
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case ONE_DAY:
                startTime = globalTimePeriod.getStartTime();
                endTime = globalTimePeriod.getEndTime();
                timePeriod.setStartEndTime(startTime, endTime);
                break;
            case AFTER_MIDNIGHT:
                timePeriod.setStartEndTime(
                        DEFAULT_AFTER_MIDNIGHT_START_TIME,
                        DEFAULT_AFTER_MIDNIGHT_END_TIME
                );
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
