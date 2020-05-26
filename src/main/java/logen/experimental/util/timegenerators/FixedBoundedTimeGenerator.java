package logen.experimental.util.timegenerators;

import java.time.LocalTime;

public class FixedBoundedTimeGenerator extends AbstractTimeGenerator {
    private final LocalTime wrapAroundTime;
    private final LocalTime baseTime;
    private final int requiredGenerationCount;

    /**
     * Constructs a generator that generates increasing time values with
     * quantity {@code requiredGenerationCount} between {@code fromTime}
     * and {@code toTime}.
     * If {@code fromTime} is after {@code toTime}, generation wraps around
     * {@code wrapAroundTime} and begins again from {@code baseTime}.
     * After {@code requiredGenerationCount} time values are generated,
     * subsequent generations will return null.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of the last time value generated
     * @param wrapAroundTime The time at which generation wraps around
     * @param baseTime The time which generation begins after wrapping around
     *                 {@code wrapAroundTime}
     * @param requiredGenerationCount The number of time values the generator
     *                                will generate
     */
    public FixedBoundedTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime,
            int requiredGenerationCount
    ) {
        super(fromTime, toTime);
        this.wrapAroundTime = wrapAroundTime;
        this.baseTime = baseTime;
        this.requiredGenerationCount = requiredGenerationCount;
    }

    /**
     * Constructs a generator that generates increasing time values with
     * quantity {@code requiredGenerationCount} between {@code fromTime}
     * and {@code toTime}.
     * After {@code requiredGenerationCount} time values are generated,
     * subsequent generations will return null.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of the last time value generated
     * @param requiredGenerationCount The number of time values the generator
     *                                will generate
     */
    public FixedBoundedTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            int requiredGenerationCount
    ) {
        super(fromTime, toTime);
        this.wrapAroundTime = null;
        this.baseTime = null;
        this.requiredGenerationCount = requiredGenerationCount;
    }

    @Override
    public LocalTime generate() {
        return null;
    }
}
