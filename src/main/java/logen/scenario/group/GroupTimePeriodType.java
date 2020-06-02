package logen.scenario.group;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GroupTimePeriodType {
    ANY,
    CUSTOM,
    @JsonProperty("EVERY DAY") EVERY_DAY,
    @JsonProperty("ONE HOUR") ONE_HOUR,
    @JsonProperty("ONE DAY") ONE_DAY,
    @JsonProperty("AFTER MIDNIGHT") AFTER_MIDNIGHT
}
