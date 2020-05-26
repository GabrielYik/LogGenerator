package logen.experimental.generation.fixed;

import logen.experimental.scenario.time.TimePeriod;

/**
 * A representation of the space between two fixed logs,
 * the space before a fixed log with only logs right adjacent,
 * or the space before a fixed log with only logs left adjacent.
 * <p>
 * A placeholder specifies the characteristics of the logs that
 * have to be generated in the space.
 * The characteristics are the number of logs and the range of
 * values the time attribute of a log can take.
 */
public class Placeholder {
    private final TimePeriod timePeriod;

    private final PlaceholderType type;
    private final int logCount;

    /**
     * Constructs a placeholder from a {@code timePeriod},
     * {@code type} and {@code logCount}.
     * @param timePeriod The time period which a log must is be in
     * @param type The nature of the number of logs to be generated
     * @param logCount The number of logs to be generated
     */
    private Placeholder(TimePeriod timePeriod, PlaceholderType type, int logCount) {
        this.timePeriod = timePeriod;
        this.type = type;
        this.logCount = logCount;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public PlaceholderType getType() {
        return type;
    }

    public int getLogCount() {
        return logCount;
    }

    public static class Builder {
        private TimePeriod timePeriod;

        private PlaceholderType type;
        private int logCount;

        public Builder() {

        }

        public Builder withTimePeriod(TimePeriod timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        public Builder withType(PlaceholderType type) {
            this.type = type;
            return this;
        }

        public Builder withLogCount(int amount) {
            this.logCount = amount;
            return this;
        }

        public Placeholder build() {
            return new Placeholder(timePeriod, type, logCount);
        }

        public PlaceholderType getType() {
            return type;
        }

        public int getLogCount() {
            return logCount;
        }
    }
}
