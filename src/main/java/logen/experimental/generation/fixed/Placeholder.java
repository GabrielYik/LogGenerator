package logen.experimental.generation.fixed;

import java.time.LocalTime;

/**
 * A representation of the space between two fixed logs,
 * before a fixed log with only logs right adjacent,
 * or after a fixed log with only logs left adjacent.
 * <p>
 * More specifically, a placeholder specifies the number of logs
 * that are supposed to be generated in the space represented,
 * and the earliest and latest time values of those logs.
 */
public class Placeholder {
    private final LocalTime startTime;
    private final LocalTime endTime;

    private final PlaceholderType type;
    private final int logCount;

    /**
     * Constructs a placeholder from a {@code startTime}, {@code endTime},
     * {@code type} and {@code logCount}.
     * @param startTime The earliest time the first log generated from this
     *                  placeholder will have
     * @param endTime The latest time the last log generated from this
     *                placeholder will have
     * @param type The nature of the number of logs to be generated
     * @param logCount The number of logs to be generated
     */
    private Placeholder(
            LocalTime startTime,
            LocalTime endTime,
            PlaceholderType type,
            int logCount
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.logCount = logCount;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public PlaceholderType getType() {
        return type;
    }

    public int getLogCount() {
        return logCount;
    }

    public static class Builder {
        private LocalTime startTime;
        private LocalTime endTime;

        private PlaceholderType type;
        private int logCount;

        public Builder() {

        }

        public Builder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
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
            return new Placeholder(startTime, endTime, type, logCount);
        }

        public PlaceholderType getType() {
            return type;
        }

        public int getLogCount() {
            return logCount;
        }

        public static Builder merge(Builder earlier, Builder later) {
            return null;
        }
    }
}
