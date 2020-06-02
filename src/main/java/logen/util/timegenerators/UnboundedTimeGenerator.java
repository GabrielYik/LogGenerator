package logen.util.timegenerators;

import java.time.LocalTime;

/**
 * A generator of time values that is not bounded by any overriding wrap
 * around time and generates increasing or decreasing time values depending
 * on its configuration.
 */
public class UnboundedTimeGenerator extends AbstractTimeGenerator {
    /**
     * The direction which generation of time values proceeds.
     */
    private final TimeGenerationDirection type;
    
    private UnboundedTimeGenerator(
            TimeGenerationDirection type,
            LocalTime fromTime,
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        super(fromTime, wrapAroundTime, baseTime);
        this.type = type;
    }

    /**
     * Constructs a generator that generates increasing time values from
     * {@code baseTime} to {@code wrapAroundTime} starting from {@code fromTime}.
     * If a time value generated is after {@code wrapAroundTime},
     * the time value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code baseTime}.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param wrapAroundTime The latest time of a time value generated
     *                       and the time at which generation wraps around
     * @param baseTime The time which generation starts from after generation
     *                 wraps around
     * @return A generator of increasing time values
     */
    public static UnboundedTimeGenerator forward(
            LocalTime fromTime, 
            LocalTime wrapAroundTime,
            LocalTime baseTime) {
        return new UnboundedTimeGenerator(
                TimeGenerationDirection.FORWARD,
                fromTime,
                wrapAroundTime,
                baseTime
        );
    }

    /**
     * Constructs a generator that generates decreasing time values from
     * {@code wrapAroundTime} to {@code baseTime} starting from {@code fromTime}.
     * If a time value generated is before {@code wrapAroundTime},
     * the time value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code baseTime}.
     *
     * @param fromTime The latest time of the first time value generated
     * @param wrapAroundTime The earliest time of a time value generated
     *                       and the time which generation wraps around
     * @param baseTime The time which generation starts from after generation
     *                 wraps around
     * @return A generator of decreasing time values
     */
    public static UnboundedTimeGenerator backward(
            LocalTime fromTime, 
            LocalTime wrapAroundTime,
            LocalTime baseTime
    ) {
        return new UnboundedTimeGenerator(
                TimeGenerationDirection.BACKWARD,
                fromTime,
                wrapAroundTime,
                baseTime
        );
    }

    @Override
    public LocalTime generate() {
        switch(type) {
            case FORWARD:
                generateForward();
            case BACKWARD:
                generateBackward();
            default:
                throw new AssertionError();
        }
    }

    private LocalTime generateForward() {
        long seconds = generateRandomSeconds();
        timeValue = timeValue.plusSeconds(seconds);
        if (timeValue.isAfter(wrapAroundTime)) {
            timeValue = baseTime;
            return generateForward();
        }
        return timeValue;
    }

    private LocalTime generateBackward() {
        long seconds = generateRandomSeconds();
        timeValue = timeValue.minusSeconds(seconds);
        if (timeValue.isBefore(wrapAroundTime)) {
            timeValue = baseTime;
            return generateBackward();
        }
        return timeValue;
    }

    private enum TimeGenerationDirection {
        FORWARD,
        BACKWARD
    }
}
