package logen.experimental.util.timegenerators;

import java.time.LocalTime;

public class FlexibleBoundedTimeGenerator extends AbstractTimeGenerator {
    private final LocalTime wrapAroundTime;
    private final LocalTime baseTime;
    private boolean hasWrappedAround;

    /**
     * Constructs a generator that generates increasing time values between
     * {@code fromTime} and {@code wrapAroundTime}.
     * If {@code fromTime} is after {@code toTime}, generation wraps around
     * {@code wrapAroundTime} and begins again from {@code baseTime}.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param toTime The latest time of the last time value generated
     * @param wrapAroundTime The time at which generation wraps around
     * @param baseTime The time which generation begins after wrapping around
     *                 {@code wrapAroundTime}
     */
    public FlexibleBoundedTimeGenerator(
            LocalTime fromTime,
            LocalTime toTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        super(fromTime, toTime);
        this.wrapAroundTime = wrapAroundTime;
        this.baseTime = baseTime;
        this.hasWrappedAround = false;
    }

    @Override
    public LocalTime generate() {
        return null;
    }
}
