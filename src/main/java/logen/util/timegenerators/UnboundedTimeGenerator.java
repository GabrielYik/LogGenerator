package logen.util.timegenerators;

import logen.util.Validation;

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
    private final TimeGenerationDirection direction;
    
    private UnboundedTimeGenerator(
            TimeGenerationDirection direction,
            LocalTime fromTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        super(fromTime, wrapAroundTime, wrapToTime);
        this.direction = direction;
    }

    /**
     * Constructs a generator that generates increasing time values from
     * {@code wrapToTime} to {@code wrapAroundTime} starting from {@code fromTime}.
     * If a time value generated is after {@code wrapAroundTime},
     * the time value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code wrapToTime}.
     *
     * @param fromTime The earliest time of the first time value generated
     * @param wrapAroundTime The latest time of a time value generated
     *                       and the time at which generation wraps around
     * @param wrapToTime The time which generation starts from after generation
     *                 wraps around
     * @return A generator of increasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if the {@code LocalTime} arguments are
     *   not in the following order: {@code wrapToTime} < {@code fromTime} <
     *   {@code wrapAroundTime}
     */
    public static UnboundedTimeGenerator forward(
            LocalTime fromTime,
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        Validation.requireNonNull(fromTime, wrapAroundTime, wrapToTime);
        Validation.requireInOrder(wrapToTime, fromTime, wrapAroundTime);

        return new UnboundedTimeGenerator(
                TimeGenerationDirection.FORWARD,
                fromTime,
                wrapAroundTime,
                wrapToTime
        );
    }

    /**
     * Constructs a generator that generates decreasing time values from
     * {@code wrapAroundTime} to {@code wrapToTime} starting from {@code fromTime}.
     * If a time value generated is before {@code wrapAroundTime},
     * the time value is discarded and generation wraps around
     * {@code wrapAroundTime} and starts from {@code wrapToTime}.
     *
     * @param fromTime The latest time of the first time value generated
     * @param wrapAroundTime The earliest time of a time value generated
     *                       and the time which generation wraps around
     * @param wrapToTime The time which generation starts from after generation
     *                 wraps around
     * @return A generator of decreasing time values
     * @throws NullPointerException if any of the {@code LocalTime} arguments
     *   are null
     * @throws IllegalArgumentException if the {@code LocalTime} arguments are
     *   not in the following order: {@code wrapToTime} < {@code fromTime} <
     *   {@code wrapAroundTime}
     */
    public static UnboundedTimeGenerator backward(
            LocalTime fromTime, 
            LocalTime wrapAroundTime,
            LocalTime wrapToTime
    ) {
        Validation.requireNonNull(fromTime, wrapAroundTime, wrapToTime);
        Validation.requireInOrder(wrapAroundTime, fromTime, wrapToTime);

        return new UnboundedTimeGenerator(
                TimeGenerationDirection.BACKWARD,
                fromTime,
                wrapAroundTime,
                wrapToTime
        );
    }

    @Override
    public LocalTime generate() {
        switch(direction) {
            case FORWARD:
                return generateForward();
            case BACKWARD:
                return generateBackward();
            default:
                throw new AssertionError();
        }
    }

    private LocalTime generateForward() {
        long seconds = generateRandomSeconds();
        timeValue = timeValue.plusSeconds(seconds);
        if (timeValue.isAfter(wrapAroundTime)) {
            timeValue = wrapToTime;
            return generateForward();
        }
        return timeValue;
    }

    private LocalTime generateBackward() {
        long seconds = generateRandomSeconds();
        timeValue = timeValue.minusSeconds(seconds);
        if (timeValue.isBefore(wrapAroundTime)) {
            timeValue = wrapToTime;
            return generateBackward();
        }
        return timeValue;
    }

    private enum TimeGenerationDirection {
        FORWARD,
        BACKWARD
    }
}
