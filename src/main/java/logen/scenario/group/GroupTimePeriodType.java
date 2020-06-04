package logen.scenario.group;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type of time period in a group.
 */
public enum GroupTimePeriodType {
    /**
     * The system determines the time period within which all logs
     * in the group are.
     */
    ANY,
    /**
     * The user determines the time period within which all logs
     * in the group are.
     */
    CUSTOM,
    /**
     * The system determines the start and end time of the one hour
     * time period within which all logs in the group are.
     */
    @JsonProperty("ONE HOUR") ONE_HOUR,
    /**
     * The user determines that all logs in the group are within
     * one day.
     */
    @JsonProperty("ONE DAY") ONE_DAY,
    /**
     * The system determines the start and end time of the after
     * midnight time period within which all logs in the group are.
     */
    @JsonProperty("AFTER MIDNIGHT") AFTER_MIDNIGHT
}
